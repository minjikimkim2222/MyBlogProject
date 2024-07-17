package org.myblog.domain.follow.repository;

import org.myblog.domain.follow.domain.Follow;

import org.myblog.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByFollowingUserAndFollowedUser(User followingUser, User followedUser);
    boolean existsByFollowingUserIdAndFollowedUserId(Long userId, Long followedUserId);
}
