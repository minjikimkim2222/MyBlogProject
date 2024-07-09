package org.myblog.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.myblog.domain.comment.domain.Comment;
import org.myblog.domain.comment.dto.CommentCreatedDTO;
import org.myblog.domain.comment.dto.CommentResponseDTO;
import org.myblog.domain.comment.dto.CommentUpdatedDTO;
import org.myblog.domain.comment.service.CommentService;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.dto.UserLoginForm;
import org.myblog.domain.user.service.UserService;
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
        log.info(" commentDTO  : {}", commentDTO);
        Comment comment = commentService.createComment(commentDTO);

        //log.info("comment : {}", comment.getId());

        if (comment != null){ // comment, post 저장 성공
            CommentResponseDTO commentResponseDTO =
                    new CommentResponseDTO(true, comment.getContent(), comment.getUpdatedAt(), comment.getId()
                    , comment.getAuthorName()); // 저장된 Comment 엔디티에서 authorName 받아오기..

            return ResponseEntity.ok(commentResponseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CommentResponseDTO(false, null, null, null, null));
        }
    }

    @PatchMapping("/{commentId}")
    @ResponseBody
    public ResponseEntity<Boolean> updateComment(@RequestBody CommentUpdatedDTO commentUpdatedDTO, @PathVariable Long commentId){
        log.info("patch requestbody 확인 -- : {}, {}", commentUpdatedDTO, commentId);
        Comment comment = commentService.updateComment(commentUpdatedDTO.getContent(), commentId);

        if (comment != null){ // comment update 성공 !!
            return ResponseEntity.status(HttpStatus.OK)
                    .body(true); // success 보냄
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(false);
        }

    }

    @DeleteMapping("/{commentId}")
    @ResponseBody
    public ResponseEntity<Boolean> deleteComment(@PathVariable Long commentId){
        //log.info("deleteComment -- commentId 전달 ?? : {}", commentId);
        commentService.deleteComment(commentId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(true);
    }
}
