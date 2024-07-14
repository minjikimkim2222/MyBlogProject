package org.myblog.web.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.user.domain.UploadFile;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.service.UserService;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FileController {
    private final FileStore fileStore;
    private final UserService userService;

    // 이미지 업로드 - 내가 지정한 경로에 파일 저장 + DB에 임베디드타입 UploadFile 저장
    @PostMapping("/users/{id}/profile-image")
    public String uploadProfileImage(@PathVariable Long id,
     @RequestParam("profileImage")MultipartFile multipartFile) throws IOException {
        log.info("profileimage - originalName : {}", multipartFile.getOriginalFilename()); // -- cat.jpg

        // 파일 저장
        UploadFile profileFile = fileStore.storeFile(multipartFile);

        // DB에 저장
        User user = userService.findById(id);
        user.setProfileFile(profileFile);
        userService.saveUser(user);

        log.info("uploadfile - uploadFileName : {}, storeFileName : {}",
                profileFile.getUploadFileName(), profileFile.getStoreFileName());
        log.info("user : {}", user);

        log.info("user.profileFile.storeFileName : {}", user.getProfileFile().getStoreFileName());
        log.info("fullpath : {}", fileStore.getFullPath(user.getProfileFile().getStoreFileName()));
        return "redirect:/settings";
    }

    // 프로필 이미지를 보여줄 컨트롤러
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource showProfileImage(@PathVariable String filename) throws MalformedURLException {
        // "file:/Users/../a90ce674-66d3-4c65-b38b-53487493aef8.js"
        log.info("filename : {}", filename);
        log.info("file path : {}", "file:" + fileStore.getFullPath(filename));

        return new UrlResource("file:" + fileStore.getFullPath(filename));
        // 해당 경로의 이미지 파일을 찾아서 업로드해줌
    }



}
