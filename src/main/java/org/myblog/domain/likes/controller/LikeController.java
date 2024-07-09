package org.myblog.domain.likes.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.likes.dto.LikeRequestDTO;
import org.myblog.domain.likes.dto.LikeResponseDTO;
import org.myblog.domain.likes.service.LikeService;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.service.PostService;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.web.login.SessionConst;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/likes")
@Slf4j
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final PostService postService;
    @PostMapping
    public ResponseEntity<?> toggleLike(@RequestBody LikeRequestDTO likeRequestDTO,
                                        @SessionAttribute(name = SessionConst.User_Login_Form, required = false) UserLoginForm userLoginForm){
        log.info("postId, : {} | userId : {}", likeRequestDTO.getPostId(), userLoginForm.getId());

        boolean liked = likeService.toggleLike(likeRequestDTO.getPostId(), userLoginForm.getId());
        Post post = postService.findById(likeRequestDTO.getPostId());

        // response -- (F/T : F(좋아요 취소)/T(좋아요 누른 상태) ) + 현재 좋아요 개수 반환
        return ResponseEntity.ok(new LikeResponseDTO(liked, post.getLikeCount()));
    }
}
