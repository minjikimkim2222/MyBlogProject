package org.myblog.domain.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.myblog.domain.blog.domain.Blog;
import org.myblog.domain.follow.domain.Follow;
import org.myblog.domain.likes.domain.Like;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String email;

    private String image; // 사용자 프로필 링크

    @Enumerated(EnumType.STRING)
    private Role role;

    // User 테이블 필드 완
    // users 테이블과 다른 테이블간의 관계매핑변수
     // users - blogs 테이블 1:1 관계
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Blog blog;

    //users - likes ; 일대다
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "followedUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers;

    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings;

    // 테스트용

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email){
        this.username = username;
        this.email = email;
    }
}
