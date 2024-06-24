package org.myblog.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.post.dto.PostCreatedDto;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.web.login.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    @GetMapping("/writeform")
    public String writeForm(Model model){
        model.addAttribute("postCreatedDto", new PostCreatedDto());
        return "post/writeForm";
    }

    @GetMapping("/writeform/next")
    public String writeFormNext(@ModelAttribute("postCreatedDto")PostCreatedDto postCreatedDto){
        log.info("postCreatedDto : {}", postCreatedDto);
        return "post/writeFormNext";
    }
}
