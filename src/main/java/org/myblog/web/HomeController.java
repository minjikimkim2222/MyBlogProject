package org.myblog.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.service.UserService;
import org.myblog.web.login.SessionConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;

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
            return "home"; // 로그인 전 페이지
        }

        // 세션에 데이터가 있다면 -- 로그인된 사용자 정보를 blog 뷰에 띄우기 위해..
        User foundUser = userService.findByUsername(userLoginForm.getUsername()); // 유저 DTO말고 엔디티를 넘겨주기.
        model.addAttribute("user", foundUser);

        // 벨로그 생성여부에 따라, 페이지 분기
        if (foundUser.getBlog() == null){
            return "redirect:/myblog/createblog"; //  블로그 생성 페이지로 리다이렉션
        }
        return "login/loginhome"; // 로그인 O, 블로그 O -> loginhome 뷰
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }
}
