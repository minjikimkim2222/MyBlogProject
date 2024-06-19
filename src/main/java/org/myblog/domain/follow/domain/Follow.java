package org.myblog.domain.follow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myblog.domain.user.domain.User;

@Entity
@Table(name = "follows")
@Getter @Setter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Follow 기준 followedUser - 다대일
    @JoinColumn(name = "followed_user_id")
    private User followedUser;

    @ManyToOne
    @JoinColumn(name = "following_user_id")
    private User followingUser;
}
