package org.myblog.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.user.domain.UploadFile;

import java.time.LocalDateTime;

@Getter @Setter
public class PostPagingResponseDTO {
    private Long id;
    private String title;
    private String subtitle;

    private String username;

    private LocalDateTime updatedAt;
    private int likeCount;

    private UploadFile previewImage;

    public PostPagingResponseDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.subtitle = post.getSubtitle();

        if (post.getBlog()!= null) {
            this.username = post.getBlog().getUser().getUsername(); // post - Blog - User 엔디티의 username
        }
        this.updatedAt = post.getUpdatedAt();
        this.likeCount = post.getLikeCount();

        this.previewImage = post.getPreviewImage();
        // UploadFile 임베디드타입의 서버에 저장된 파일명 (String, storeFileName)을 얻어와서 세팅 !!
    }
}
