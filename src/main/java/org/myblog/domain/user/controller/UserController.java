package org.myblog.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.service.UserService;
import org.myblog.web.file.FileStore;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final FileStore fileStore;

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

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        User foundUser = userService.findById(id);

        log.info("id : {}", id);
        log.info("foundUser : {}", foundUser);

        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }

    // 프로필 이미지 삭제
    @DeleteMapping("/users/{id}/profile-image")
    public String deleteUserProfileImage(@PathVariable Long id){
        User user = userService.findById(id);

        log.info("User : {}", user);

        if (user.getProfileFile() == null){ // 이미 프로필 사진이 없음
            log.warn("User {} has no profile image to delete.", id);
        } else {
            // 프로필 사진이 있는 경우, 파일 삭제를 할 것
            String filePath = fileStore.getFullPath(user.getProfileFile().getStoreFileName());
            log.info("filePath : {}", filePath);
            boolean isDeleted = deleteFile(filePath);

            if (isDeleted){
                user.setProfileFile(null); // 프로필 이미지를 null로 설정
                userService.saveUser(user); // DB에 유저 정보 update !!
            } else {
                log.error("Failed to delete profile image for user {}", id);
            }
        }

        return "redirect:/settings";
    }

    private boolean deleteFile(String filePath){
        File file = new File(filePath);

        if (file.exists()){
            return file.delete();
        } else {
            log.warn("File {} does not exist. so, cannot delete", filePath);
            return false;
        }
    }
}
