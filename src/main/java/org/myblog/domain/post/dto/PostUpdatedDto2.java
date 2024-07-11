package org.myblog.domain.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.myblog.domain.user.domain.UploadFile;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class PostUpdatedDto2 {

    private MultipartFile previewImage; // 프로필 파일  -- MultipartFile 타입으로 변경 ..

    private String subtitle; // 부제목 -- 당신의 포스트를 짧게 소개해보세요..

    private Boolean visibility; // 공개글 여부 (true - 공개글, false - 비공개글)

    private String storeFileName; // 이미지 파일 업로드용

    private String seriesName = null; // post가 속한 시리즈 이름 -- null일수도 있음 (기본적으로 null로 세팅해둠)

}
