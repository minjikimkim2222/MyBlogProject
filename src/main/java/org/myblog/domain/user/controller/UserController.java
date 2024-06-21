package org.myblog.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @GetMapping("/users/{id}")
    public String updateUsernameForm(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        log.info("User Entity : {}", user);
        String username = null;
        model.addAttribute("user", user);

        return "/user/editUser";
    }

    @PatchMapping("/users/{id}")
    public String updateUsername(@PathVariable Long id, @ModelAttribute("user") User user){
        // User 엔디티에 username 수정 로직 추가 !!
//        log.info("User toString >>> : {}", user);
        User founduser = userService.findById(id);
        founduser.setUsername(user.getUsername()); // username 값 바꿔주기

        log.info("User info : {}", founduser);
        userService.saveUser(founduser); // 없으면 insert, 있으면 update

        return "redirect:/settings";
    }
}
