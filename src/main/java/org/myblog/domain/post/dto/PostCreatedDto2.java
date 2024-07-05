package org.myblog.domain.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.myblog.domain.user.domain.UploadFile;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class PostCreatedDto2 {

    private MultipartFile previewImage; // 프로필 파일  -- MultipartFile 타입으로 변경 ..

    private String subtitle; // 부제목 -- 당신의 포스트를 짧게 소개해보세요..

    private Boolean visibility; // 공개글 여부 (true - 공개글, false - 비공개글)

    private Long postId;

}
