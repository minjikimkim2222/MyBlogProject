package org.myblog.domain.likes.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.user.domain.User;

@Entity
@Table(name = "likes")
@Getter @Setter
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // like기준, post와 다대일
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne // like기준, user와 다대일
    @JoinColumn(name = "user_id")
    private User user;
}
