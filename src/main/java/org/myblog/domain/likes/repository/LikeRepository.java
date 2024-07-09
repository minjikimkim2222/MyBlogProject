package org.myblog.domain.likes.repository;

import org.myblog.domain.likes.domain.Like;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
}
