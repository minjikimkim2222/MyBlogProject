package org.myblog.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class PostCreatedDto {

    private String title;

    private List<String> tags;

    private String content;

    private Boolean published; // 임시글 여부

}
