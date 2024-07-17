package org.myblog.domain.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.user.domain.UploadFile;

import java.time.LocalDateTime;

@Getter @Setter
public class PostSearchDto {
    private UploadFile previewImage;
    private String username;
    private String content;
    private String title;
    private String subtitle;
    private LocalDateTime updatedAt;
    private int likeCount;

    public PostSearchDto(Post post){
        previewImage = post.getPreviewImage();

        if (post.getBlog() != null){
            username = post.getBlog().getUser().getUsername();
        }

        content = post.getContent();
        title = post.getTitle();
        subtitle = post.getSubtitle();
        updatedAt = post.getUpdatedAt();
        likeCount = post.getLikeCount();
    }
}
