package org.myblog.web.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
                        BindingResult bindingResult){

        log.info("loginform : {}", form);

        boolean isLoginSuccess = userService.login(form.getUsername(), form.getPassword());

        if (!isLoginSuccess) {
            // 로그인 실패했으면, 에러메시지를 bindingResult에 추가해줄 것
            bindingResult.reject("totalLoginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            log.info("bindingResult : {}", bindingResult);
            return "login/loginForm"; // 다시 로그인 폼으로 가서, 에러메시지 보여줄 것
        }

        // 로그인 성공
        String username = form.getUsername();
        return "redirect:/myblog/@" + username;
    }
}
