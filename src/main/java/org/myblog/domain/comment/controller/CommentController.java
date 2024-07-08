package org.myblog.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.comment.domain.Comment;
import org.myblog.domain.comment.dto.CommentCreatedDTO;
import org.myblog.domain.comment.dto.CommentResponseDTO;
import org.myblog.domain.comment.service.CommentService;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.web.login.SessionConst;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseBody
    public ResponseEntity<?> createComment(@RequestBody CommentCreatedDTO commentDTO){
        // log.info("commentDTO : {}", commentDTO);
        Comment comment = commentService.createComment(commentDTO);
        log.info("comment : {}", comment.getId());

        if (comment != null){ // comment, post 저장 성공
            CommentResponseDTO commentResponseDTO = new CommentResponseDTO(true, comment.getContent(), comment.getUpdatedAt());

            return ResponseEntity.ok(commentResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommentResponseDTO(false, null, null));
        }
    }
}
