package org.myblog.domain.likes.repository;

import org.junit.jupiter.api.Test;
import org.myblog.domain.likes.domain.Like;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.repository.PostRepository;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LikeRepositoryTest {
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    void findByPostIdAndUserId() {
        // 사용자와 포스트 생성
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("test@example.com");

        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("This is a test post.");

        // 사용자와 포스트 저장
        userRepository.save(user);
        postRepository.save(post);

        // 좋아요 생성
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeRepository.save(like);

        // 좋아요 찾기
        Optional<Like> foundLike = likeRepository.findByPostIdAndUserId(post.getId(), user.getId());

        // 결과 출력
        if (foundLike.isPresent()) {
            System.out.println("foundLike -- id ::: " + foundLike.get().getId());
            System.out.println("foundLike -- user ::: " + foundLike.get().getUser().getUsername());
            System.out.println("foundLike -- post ::: " + foundLike.get().getPost().getTitle());
        } else {
            System.out.println("Like not found");
        }
    }
}