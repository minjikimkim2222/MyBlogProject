package org.myblog.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.dto.PostCreatedDto;
import org.myblog.domain.post.dto.PostCreatedDto2;
import org.myblog.domain.post.repository.PostRepository;
import org.myblog.domain.post.service.PostService;
import org.myblog.domain.series.domain.Series;
import org.myblog.domain.series.service.SeriesService;
import org.myblog.domain.tag.domain.Tag;
import org.myblog.domain.tag.service.TagService;
import org.myblog.domain.user.domain.UploadFile;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.service.UserService;
import org.myblog.web.file.FileStore;
import org.myblog.web.login.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final TagService tagService;
    private final PostService postService;
    private final FileStore fileStore;
    private final SeriesService seriesService;
    private final UserService userService;

    @GetMapping("/writeform")
    public String writeForm(Model model){
        model.addAttribute("postCreatedDto", new PostCreatedDto());
        return "post/writeForm";
    }

    @PostMapping("/writeform/next")
    public String writeFormNext(@ModelAttribute PostCreatedDto postCreatedDto){

        Post post = new Post();
        post.setTitle(postCreatedDto.getTitle());
        post.setContent(postCreatedDto.getContent());
        post.setPublished(postCreatedDto.getPublished());

        // post 1차 저장해야 postId가 생성됨
        Post savedPost = postService.savePost(post);

        // '태그'엔디티 저장 및 연관관계 설정
        List<String> tagNames = postCreatedDto.getTags();
        List<Tag> tags = tagNames.stream()
                .map(tagService::saveTagByName) // 태그 저장하거나, 기존 태그 반환
                .collect(Collectors.toList());

        // 저장된 Tag와 Post 연관관계 설정 -- POST_TAG
        savedPost.setTags(tags);

        postService.savePost(savedPost); // -- 연관관계 설정 후, 다시 저장 (값이 update될 것)

        return "redirect:/writeform/next/step/" + savedPost.getId();
    }

    // postId로 2차 저장 폼 요청해야 됨 !! -- 여기서 Post 엔디티 값이 채워졌는지 체크
    @GetMapping("/writeform/next/step/{postId}")
    public String writeFormNextStep(@PathVariable Long postId, Model model){
        PostCreatedDto2 postCreatedDto2 = new PostCreatedDto2();
        postCreatedDto2.setVisibility(true); // 기본값 설정 (전체공개)
        postCreatedDto2.setPostId(postId); // -- 히든변수로 설정해서 값 변경 불가하게끔 !!

        model.addAttribute("postCreatedDto2",postCreatedDto2);

        return "post/writeFormNext";
    }

    @PostMapping("/writeform/next/step")
    public String writeFormNextStep(@ModelAttribute(name = "postCreatedDto2") PostCreatedDto2 postCreatedDto2,
        @SessionAttribute(name = SessionConst.User_Login_Form, required = false)UserLoginForm userLoginForm,
        @RequestParam(name = "seriesName")String seriesName) throws IOException {

        // 파일 저장
        MultipartFile multipartFile = postCreatedDto2.getPreviewImage();
        UploadFile previewImage = null;
        if (multipartFile != null && !multipartFile.isEmpty()){
            previewImage = fileStore.storeFile(multipartFile);
        }


        // DB에 저장
        Post post = postService.findById(postCreatedDto2.getPostId());
        post.setPreviewImage(previewImage);
        post.setSubtitle(postCreatedDto2.getSubtitle());
        post.setVisibility(postCreatedDto2.getVisibility());
        post.setBlog(postService.findBlogByUserLoginForm(userLoginForm));

        // 시리즈를 선택하지 않을수도 있음. 그냥 단순한 Post 일수도..
        if (seriesName != null && !seriesName.isEmpty()){
            Series series = seriesService.findBySeriesName(seriesName);

            if (series == null){ // 새로운 시리즈 저장
                series = new Series();
                series.setSeriesName(seriesName);
                seriesService.save(series);
            }

            post.setSeries(series); // Post - Series 연관관계 설정
        }

        postService.savePost(post); // post 2차까지 DB에 저장완료

        User user = userService.findById(userLoginForm.getId());

        // redirect할 때, username은 변경할 수 있으니까 안 바뀌는 'name' 속성으로 !!
        String encodedUsername = URLEncoder.encode(user.getName(), StandardCharsets.UTF_8);
        String encodedPostTitle = URLEncoder.encode(post.getTitle(), StandardCharsets.UTF_8);

        return "redirect:/@" + encodedUsername + "/" + encodedPostTitle;
    }

    @GetMapping("/@{encodedUsername}/{encodedPostTitle}")
    public String showPost(@PathVariable String encodedUsername, @PathVariable String encodedPostTitle, Model model){
        String decodedUsername = URLDecoder.decode(encodedUsername, StandardCharsets.UTF_8);
        String decodedPostTitle = URLDecoder.decode(encodedPostTitle, StandardCharsets.UTF_8);

        log.info("decodedUsername : {}", decodedUsername);
        log.info("decodedPostTitle : {}", decodedPostTitle);

        Post post = postService.findByTitle(decodedPostTitle); // post가 null일 경우, 에러 발생시키도록 postService에 설정해둠..
        User user = userService.findByName(decodedUsername);// 이름만 username이고 넘긴 건 'name' -- 역시 에러 처리 userservice에 설정

        model.addAttribute("post", post);
        model.addAttribute("user", user);

        return "post/showPost";
    }


}
