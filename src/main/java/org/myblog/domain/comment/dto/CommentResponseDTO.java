package org.myblog.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.myblog.domain.comment.domain.Comment;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponseDTO {
    private boolean success;
    private String content;
    private LocalDateTime updatedAt;
    // 댓글 수정하며 추가
    private Long commentId;

    private String authorName; // 댓글 작성 당시, 로그인한 유저닉네임 !!
}
