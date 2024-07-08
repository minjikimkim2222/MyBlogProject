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
}
