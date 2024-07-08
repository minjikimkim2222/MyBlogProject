package org.myblog.domain.comment.dto;

import lombok.Data;

@Data
public class CommentCreatedDTO {
    private Long postId;
    private String content;
}
