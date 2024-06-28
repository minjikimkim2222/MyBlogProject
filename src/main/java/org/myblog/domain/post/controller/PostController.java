package org.myblog.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.dto.PostCreatedDto;
import org.myblog.domain.post.repository.PostRepository;
import org.myblog.domain.tag.domain.Tag;
import org.myblog.domain.tag.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final TagService tagService;
    private final PostRepository postRepository;

    @GetMapping("/writeform")
    public String writeForm(Model model){
        model.addAttribute("postCreatedDto", new PostCreatedDto());
        return "post/writeForm";
    }

    @GetMapping("/writeform/next")
    public String writeFormNext(@ModelAttribute("postCreatedDto")PostCreatedDto postCreatedDto){
        log.info("postCreatedDto : {}", postCreatedDto);

        Post post = new Post();
        post.setTitle(postCreatedDto.getTitle());
        post.setContent(postCreatedDto.getContent());
        post.setPublished(postCreatedDto.getPublished());

        List<Tag> tagDomainList = new ArrayList<>();
        for (String tagName : postCreatedDto.getTags()) {
            Tag tag = tagService.findByTagName(tagName);
            tagDomainList.add(tag);
        }

        post.setTags(tagDomainList);

        // 태그 삭제 기능구현 때문에, 여기서 Post 1차 저장하고..
        postRepository.save(post);

        return "post/writeFormNext";
    }

//    @PostMapping("/writeform") // -- 여기서 Post 2차 저장할 때, findById로 Post를 찾고 값 세팅해줘야 함 !
//    public String writeformFinal(){
//
//    }
}
