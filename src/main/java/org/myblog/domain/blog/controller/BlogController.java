package org.myblog.domain.blog.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.blog.domain.Blog;
import org.myblog.domain.blog.dto.BlogCreateForm;
import org.myblog.domain.blog.service.BlogService;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.service.UserService;
import org.myblog.web.login.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/myblog")
@RequiredArgsConstructor
@Slf4j
public class BlogController {

    private final UserService userService;
    private final BlogService blogService;
    @GetMapping("/blogs")
    public String blogForm(Model model){
        model.addAttribute("blogCreateForm", new BlogCreateForm());
        return "blog/blogForm";
    }

    @PostMapping("/blogs")
    public String createBlog(@Validated @ModelAttribute("blogCreateForm")BlogCreateForm form,
                             BindingResult bindingResult,
                             @SessionAttribute(name = SessionConst.User_Login_Form, required = false)UserLoginForm userLoginForm){ // 블로그 생성

        log.info("Blog : {}", form); // -- 블로그 폼 잘 전달받았는지..
        log.info("Session으로부터 받은 유저 로그인 정보 : {} ", userLoginForm);

        if (bindingResult.hasErrors()){
            return "redirect:/myblog/blogs"; // 타이틀 다시 입력하게끔
        }

        Blog blog = new Blog();
        blog.setTitle(form.getTitle());
        blog.setUser(userService.findByUsername(userLoginForm.getUsername())); // 세션에 저장된 유저name을 기반으로, 블로그에 유저 넣어주기
        blog.setPosts(null); // 아직 새 블로그를 만드는 단계인데, 포스트가 있을리 없음

        blogService.saveBlog(blog);

        // 블로그 생성 완료 (로그인 O, 블로그 생성 완 O)
        return "login/loginhome"; // 그제서야 기타 기능 사용 가능 !!
    }
}
