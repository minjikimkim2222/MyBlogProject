package org.myblog.domain.follow.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myblog.domain.user.domain.User;

@Entity
@Table(name = "follows")
@Getter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Follow 기준 followedUser - 다대일
    @JoinColumn(name = "followed_user_id")
    private User followedUser; // 팔로우받는 사람 -- 현재 로그인한 내가 팔로우 누른 대상

    @ManyToOne
    @JoinColumn(name = "following_user_id")
    private User followingUser; // 팔로잉하는 사람 -- 현재 로그인한 나

    // 원래 연관관계 메서드
    public void setFollowingUser(User followingUser){
        this.followingUser = followingUser;
        followingUser.getFollowings().add(this);
    }

    public void setFollowedUser(User followedUser){
        this.followedUser = followedUser;
        followedUser.getFollowers().add(this);
    }

    // 생성메서드
    public static Follow createFollow(User followingUser, User followedUser){ // id1, id2
        Follow follow = new Follow();

        follow.setFollowingUser(followingUser); // id1 -- followingUser(id2)
        follow.setFollowedUser(followedUser); // id2 -- followedUser(id1)

        return follow;
    }
}
