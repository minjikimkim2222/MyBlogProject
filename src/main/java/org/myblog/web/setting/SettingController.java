package org.myblog.web.setting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SettingController {

    private final UserService userService;
    @GetMapping("/settings")
    public String settingForm(
            @SessionAttribute(name = SessionConst.User_Login_Form, required = false) UserLoginForm userLoginForm
            ,Model model){

        User user = userService.findById(userLoginForm.getId());

        model.addAttribute("user", user);

        log.info("userLoginForm : {}", userLoginForm);
        log.info("User : {}", user);
        log.info("User - blog : {}", user.getBlog().getTitle());

        return "setting/settingForm";
    }
}
