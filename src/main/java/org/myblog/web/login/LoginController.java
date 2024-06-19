package org.myblog.web.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;
    @GetMapping("/loginform")
    public String loginForm(Model model){
        model.addAttribute("userLoginDTO", new UserLoginForm());
        return "login/loginForm"; // 로그인 폼 요청
    }

    @PostMapping("/login") // 실제 로그인 기능
    public String login(@Validated @ModelAttribute("userLoginDTO")UserLoginForm form,
                        BindingResult bindingResult, HttpServletRequest request){

        log.info("loginform : {}", form);

        UserLoginForm userLoginForm = userService.login(form.getUsername(), form.getPassword());

        if (userLoginForm == null) {
            // 로그인 실패했으면, 에러메시지를 bindingResult에 추가해줄 것
            bindingResult.reject("totalLoginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult : {}", bindingResult);
            return "login/loginForm"; // 다시 로그인 폼으로 가서, 에러메시지 보여줄 것
        }

        // 로그인 성공 처리 - 서블릿이 제공하는 HttpSession 적용 !!

        // 세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성 (디폴트값 true)
        HttpSession session = request.getSession();

        // 세션 id - (SessionConst에 상수화한 "userLoginForm")
        // 세션 value - userLoginForm 객체
        session.setAttribute(SessionConst.User_Login_Form, userLoginForm);

        String username = userLoginForm.getUsername();
        return "redirect:/myblog/@" + username;
    }

    @GetMapping("/myblog/@{username}")
    public String userProfile(@PathVariable String username,
      @SessionAttribute(name=SessionConst.User_Login_Form, required = false) UserLoginForm userLoginForm,
      Model model){
        // 세션에 데이터가 없으면 home 뷰로 -- 로그인 안했다면..
        if (userLoginForm == null){
            return "home";
        }

        // 세션에 데이터가 있다면 -- 로그인된 사용자 정보를 blog 뷰에 띄우기 위해..
        User foundUser = userService.findByUsername(userLoginForm.getUsername()); // 유저 DTO말고 엔디티를 넘겨주기.

        model.addAttribute("user", foundUser);
        return "login/blog";
    }
}
