package org.myblog.web.register;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.user.domain.Role;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserRegisterForm;
import org.myblog.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RegisterController {

    private final UserService userService;

    @GetMapping("/userreg")
    public String registerForm(Model model){
        String passwordCheck = null;
        model.addAttribute("userRegisterDTO", new UserRegisterForm());
        return "register/registerForm"; // 회원가입 폼 요청
    }

    @PostMapping("/userreg")
    public String register(@Validated @ModelAttribute("userRegisterDTO")UserRegisterForm form,
                           BindingResult bindingResult){

        log.info("form : {}", form); //-- UserRegisterForm을 잘 넘김..

        // 비밀번호 확인
        if (!userService.CheckPassword(form.getPassword(), form.getPasswordCheck())) {
            bindingResult.reject("totalPasswordError", "비밀번호가 일치하지 않습니다.");
        }

        // 아이디 중복 검사
        if (userService.isUsernameDuplicate(form.getUsername())) {
            bindingResult.rejectValue("username", "usernameDuplicate", "이미 사용중인 아이디입니다.");
        }

        // 이메일 중복 검사
        if (userService.isEmailDuplicate(form.getEmail())) {
            bindingResult.rejectValue("email", "emailDuplicate", "이미 사용중인 이메일입니다.");
        }

        if (bindingResult.hasErrors()){
            log.info(">> bindingResult : {}", bindingResult);
            return "register/registerForm"; // 검증 실패 시, 다시 회원가입폼 입력해주세요
        }

        // 검증 성공 로직
        User user = new User();
        user.setUsername(form.getUsername());
        user.setPassword(form.getPassword());
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setImage(null); // -- 이미지 url 일단 null로 해둠
        user.setRole(Role.ROLE_USER); // -- 일단 디폴트로 ROLE_USER -- 일반 유저로 등록

        User savedUser = userService.saveUser(user);

        return "redirect:/";
    }
}
