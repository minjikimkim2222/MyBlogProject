package org.myblog.domain.blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BlogCreateForm {

    @NotBlank(message = "블로그 이름은 필수 입력 값입니다.")
    private String title;
}
