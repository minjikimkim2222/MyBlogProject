package org.myblog.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    @GetMapping("/users/{id}/username")
    public String updateUsernameForm(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        log.info("User Entity : {}", user);
        model.addAttribute("user", user);

        return "/user/editUsernameForm";
    }

    @GetMapping("/users/{id}/email")
    public String updateEmailForm(@PathVariable Long id, Model model){
        User user = userService.findById(id);
        //log.info("User Entity : {}", user);
        model.addAttribute("user", user);

        return "/user/editEmailForm";
    }

    @PatchMapping("/users/{id}/username")
    public String updateUsername(@PathVariable Long id, @ModelAttribute("user") User user){
        // User 엔디티에 username 수정 로직 추가 !!
//        log.info("User toString >>> : {}", user);
        User founduser = userService.findById(id);
        founduser.setUsername(user.getUsername()); // username 값 바꿔주기

        log.info("User info : {}", founduser);
        userService.saveUser(founduser); // 없으면 insert, 있으면 update

        return "redirect:/settings";
    }

    @PatchMapping("/users/{id}/email")
    public String updateEmail(@PathVariable Long id, @ModelAttribute("user") User user){
        // User 엔디티에 username 수정 로직 추가 !!
//        log.info("User toString >>> : {}", user);
        User founduser = userService.findById(id);
        founduser.setEmail(user.getEmail()); // email 값 바꿔주기

        log.info("User info : {}", founduser);
        userService.saveUser(founduser); // 없으면 insert, 있으면 update

        return "redirect:/settings";
    }

    @PatchMapping("/users/{id}/notification-settings")
    public String updateNotificationSettings(@PathVariable Long id,
         @RequestParam(name="commentNotification", required = false) boolean commentNotification,
         @RequestParam(name="updateNotification", required = false) boolean updateNotification){

        log.info("댓글 알림 여부 : {}", commentNotification);
        log.info("업데이트 여부 : {}", updateNotification);

        User foundUser = userService.findById(id);
        foundUser.setCommentNotification(commentNotification);
        foundUser.setUpdateNotification(updateNotification);

        userService.saveUser(foundUser); // 없으면 insert, 있다면 update

        return "redirect:/settings";
    }
}
