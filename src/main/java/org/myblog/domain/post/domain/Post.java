package org.myblog.domain.post.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.myblog.domain.blog.domain.*;
import org.myblog.domain.comment.domain.Comment;
import org.myblog.domain.likes.domain.Like;
import org.myblog.domain.series.domain.Series;
import org.myblog.domain.tag.domain.Tag;
import org.myblog.domain.user.domain.UploadFile;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "posts")
@Getter @Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // @CreatedDate, @LastModifiedDate 추가를 위함..
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String subtitle;
    private String content;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    private Boolean published; // 임시글 여부

    private Boolean visibility; // 공개글 여부 (true - 공개글, false - 비공개글)

    @Embedded
    private UploadFile previewImage; // post -- 미리보기 이미지

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 칼럼 설정완. 추가 관계설정..
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    private int likeCount; // 추가한 필드 -- 좋아요 기능

    @ManyToMany
    @JoinTable( // posts - tags 다대다 -- Post가 연관관계 주인
        name = "post_tags",
        joinColumns = @JoinColumn(name = "post_id"), // 지금 Post 테이블 기준으로 조인칼럼
        inverseJoinColumns = @JoinColumn(name = "tag_id") // 반대쪽의 Tag 테이블 기준으로
    )
    private List<Tag> tags;

}
