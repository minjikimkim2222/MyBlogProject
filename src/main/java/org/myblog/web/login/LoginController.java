package org.myblog.web.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/loginform")
    public String loginForm(){
        return "login/loginForm";
    }
}
