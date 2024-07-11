package org.myblog.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.blog.domain.Blog;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.dto.PostCreatedDto;
import org.myblog.domain.post.dto.PostCreatedDto2;
import org.myblog.domain.post.dto.PostUpdatedDto;
import org.myblog.domain.post.dto.PostUpdatedDto2;
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
import org.springframework.http.HttpStatus;
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

    // 만들어진 Post 엔디티 보여주는 화면
    @GetMapping("/@{encodedUsername}/{encodedPostTitle}")
    public String showPost(@PathVariable String encodedUsername, @PathVariable String encodedPostTitle, Model model,
                           @SessionAttribute(name = SessionConst.User_Login_Form, required = false)UserLoginForm userLoginForm){
        String decodedUsername = URLDecoder.decode(encodedUsername, StandardCharsets.UTF_8);
        String decodedPostTitle = URLDecoder.decode(encodedPostTitle, StandardCharsets.UTF_8);

//        log.info("decodedUsername : {}", decodedUsername);
//        log.info("decodedPostTitle : {}", decodedPostTitle);

        Post post = postService.findByTitle(decodedPostTitle); // post가 null일 경우, 에러 발생시키도록 postService에 설정해둠..
        User userBlog = userService.findByName(decodedUsername);// 이름만 username이고 넘긴 건 'name' -- 역시 에러 처리 userservice에 설정
        User userSession = userService.findById(userLoginForm.getId());


        model.addAttribute("post", post); // 저장된 post 정보
        model.addAttribute("userBlog", userBlog); // 블로그에 매핑된 유저정보
        model.addAttribute("userSession", userSession); // 현재 로그인한 유저정보


        return "post/showPost";
    }

    // post 수정 폼 요청
    @GetMapping("/editForm/{postId}")
    public String editForm(Model model, @PathVariable Long postId){
        Post post = postService.findById(postId);

        PostUpdatedDto postUpdatedDto = new PostUpdatedDto();
        postUpdatedDto.setTitle(post.getTitle());
        postUpdatedDto.setContent(post.getContent());
        postUpdatedDto.setTags(post.getTags().stream().map(Tag::getTagName).collect(Collectors.toList()));
        postUpdatedDto.setPublished(post.getPublished());

        model.addAttribute("postUpdatedDto", postUpdatedDto);
        model.addAttribute("postId", postId);
        return "post/editForm";
    }

    @PostMapping("/editForm/{postId}")
    public String editFormNext(@PathVariable Long postId, @ModelAttribute PostUpdatedDto postUpdatedDto){

        // 1. JPA는 해당 ID를 가진 엔디티를, 영속성 컨텍스트로 가져오기에, 해당 post는 영속화상태가 됨!!
        Post post = postService.findById(postId);

        // 2. 조회된 Post 엔디티의 필드를 업데이트 !
        post.setTitle(postUpdatedDto.getTitle());
        post.setContent(postUpdatedDto.getContent());
        post.setPublished(postUpdatedDto.getPublished());

        // 수정한 태그 리스트를 받고, 업데이트 (tagService::saveTagByName -- 이미 태그 있다면 기존태그반환, 없어야 새로운 태그 엔디티 생성)
        List<String> updatedTagNames = postUpdatedDto.getTags();
        List<Tag> updatedTags = updatedTagNames.stream()
                .map(tagService::saveTagByName)
                .collect(Collectors.toList());
        post.setTags(updatedTags); // post - tags 연관관계 설정

        log.info("1차 수정 적용 post :: {}", post);
        // 3. 업데이트된 Post 엔디티 저장
        postService.savePost(post);

        return "redirect:/editForm/next/step/" + post.getId(); // -- 2차 post 수정 폼으로 redirect
    }

    @GetMapping("/editForm/next/step/{postId}")
    public String editFormNextStep(@PathVariable Long postId, Model model){
        Post post = postService.findById(postId);
        PostUpdatedDto2 postUpdatedDto2 = new PostUpdatedDto2();

        postUpdatedDto2.setSubtitle(post.getSubtitle());
        postUpdatedDto2.setVisibility(post.getVisibility());
        if (post.getPreviewImage() != null) {
            postUpdatedDto2.setStoreFileName(post.getPreviewImage().getStoreFileName()); // -- 이미 storeFile명이 존재한다면, 저장된 파일이미지 띄워야하니깐요
        }
        // post가 시리즈에 속하지 않을수도 있음 !
        String seriesName = post.getSeries().getSeriesName();
        if (seriesName != null) {
            postUpdatedDto2.setSeriesName(seriesName); // 시리즈가 있어야, 해당 값으로 채워짐..
        }

        log.info("1차 edit 상태 ::: postUpdatedDto2 : {}", postUpdatedDto2);

        model.addAttribute("postUpdatedDto2", postUpdatedDto2);
        model.addAttribute("postId", postId);

        return "post/editFormNext";
    }

    @PostMapping("/editForm/next/step/{postId}")
    public String editFormNextStep(@PathVariable Long postId, @ModelAttribute PostUpdatedDto2 postUpdatedDto2,
                                   @SessionAttribute(name = SessionConst.User_Login_Form, required = false)UserLoginForm userLoginForm) throws IOException {
        log.info("최종 2차수정된 post ::: {}", postUpdatedDto2);

        // 파일 저장
        MultipartFile multipartFile = postUpdatedDto2.getPreviewImage();
        UploadFile previewImage = null;

        if (multipartFile != null && !multipartFile.isEmpty()){
            previewImage = fileStore.storeFile(multipartFile);
        }

        // DB에 저장
        Post post = postService.findById(postId);
        post.setPreviewImage(previewImage);
        post.setSubtitle(postUpdatedDto2.getSubtitle());
        post.setVisibility(postUpdatedDto2.getVisibility());

        // 시리즈
        String seriesName = postUpdatedDto2.getSeriesName();
        if (seriesName != null && !seriesName.isEmpty()){
            Series series = seriesService.findBySeriesName(seriesName);

            if (series == null){ // 시리즈 없으면, 만들기
                series = new Series();
                series.setSeriesName(seriesName);
                seriesService.save(series); // 새로운 시리즈 DB에 저장
            }

            post.setSeries(series); // post - series 연관관계 설정..
        }


        // 수정된 post까지 DB에 저장
        Post updatedPost = postService.savePost(post);

        User user = userService.findById(userLoginForm.getId());

        // redirect를 위한 url 인코딩
        String encodedUsername = URLEncoder.encode(user.getName(), StandardCharsets.UTF_8);
        String encodedPostTitle = URLEncoder.encode(updatedPost.getTitle(), StandardCharsets.UTF_8);

        return "redirect:/@" + encodedUsername + "/" + encodedPostTitle;
    }

    @DeleteMapping("/posts/{postId}")
    @ResponseBody
    public ResponseEntity<?> deletePost(@PathVariable Long postId){
        Post post = postService.findById(postId);

        if (post != null){
            postService.deletePost(post);
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found with postId: " + postId);
    }


    // 내 벨로그 화면
    @GetMapping("/@{encodedUsername}/posts")
    public String showMyVelogPage(@PathVariable String encodedUsername, Model model,
                                  @SessionAttribute(name = SessionConst.User_Login_Form, required = false)UserLoginForm userLoginForm){

        User user = userService.findByName(encodedUsername);
        Blog blog = postService.findBlogByUserLoginForm(userLoginForm);

        List<Post> posts = blog.getPosts();

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("encodedUsername", encodedUsername);

        return "post/showMyVelog";
    }
}
