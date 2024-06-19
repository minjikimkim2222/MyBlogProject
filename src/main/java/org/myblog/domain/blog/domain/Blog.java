package org.myblog.domain.blog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.user.domain.User;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "blogs")
@Getter @Setter
@NoArgsConstructor
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt; // 생성날짜를 자동으로 매핑시켜주고 싶은 엔디티에 사용하면 됨

    @OneToMany(mappedBy = "blog")
    private List<Post> posts;
}
