package org.myblog.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.service.PostService;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.service.UserService;
import org.myblog.web.login.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/")
    public String root(){
        return "redirect:/myblog";
    }

    @GetMapping("/myblog")
    public String home(
            @SessionAttribute(name = SessionConst.User_Login_Form, required = false)UserLoginForm userLoginForm,
            Model model){
        // 세션에 데이터가 없으면 home 뷰로 -- 로그인 안된 상태
        if (userLoginForm == null){
            // 로그인 안된 상태더라도, 다른 사람들의 post 화면 확인가능
            List<Post> posts = postService.findAllPosts();

            model.addAttribute("posts", posts);
            log.info("post 개수 :: {}", posts.size());

            return "home"; // 로그인 전 페이지
        }

        // 세션에 데이터가 있다면 -- 로그인된 사용자 정보를 blog 뷰에 띄우기 위해..
        User foundUser = userService.findById(userLoginForm.getId());

        model.addAttribute("user", foundUser);

        // 벨로그 생성여부에 따라, 페이지 분기
        if (foundUser.getBlog() == null){
            return "redirect:/myblog/blogs"; //  블로그 생성 페이지로 리다이렉션
        }

        return "redirect:/myblog/home"; // 로그인 O, 블로그 O -> loginhome 뷰
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
