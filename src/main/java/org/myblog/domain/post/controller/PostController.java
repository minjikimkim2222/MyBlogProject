package org.myblog.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.dto.PostCreatedDto;
import org.myblog.domain.post.repository.PostRepository;
import org.myblog.domain.post.service.PostService;
import org.myblog.domain.tag.domain.Tag;
import org.myblog.domain.tag.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    // postId로 2차 저장 폼 요청해야 됨 !!
    @GetMapping("/writeform/next/step/{postId}")
    public String writeFormNextStep(@PathVariable Long postId){
        return "post/writeFormNext";
    }
}
