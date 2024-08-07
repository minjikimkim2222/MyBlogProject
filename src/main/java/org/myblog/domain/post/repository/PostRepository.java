package org.myblog.domain.post.repository;

import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.repository.customRepository.CustomPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// -- Custom Repository 또한 PostRepository 에 통합시킴
@Repository
public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    Optional<Post> findByTitle(String title);

    // 페이징 처리를 위한 레포지토리 추가
    Page<Post> findAllByOrderByLikeCountDesc(Pageable pageable);
    Page<Post> findAllByOrderByUpdatedAtDesc(Pageable pageable);


}
