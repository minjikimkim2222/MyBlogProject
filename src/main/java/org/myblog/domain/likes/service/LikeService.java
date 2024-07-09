package org.myblog.domain.likes.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.likes.domain.Like;
import org.myblog.domain.likes.repository.LikeRepository;
import org.myblog.domain.post.domain.Post;
import org.myblog.domain.post.exception.PostNotFoundException;
import org.myblog.domain.post.repository.PostRepository;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /*
        이미 좋아요가 눌러져있음 == Like엔디티 존재 -> (좋아요 취소) -- Like 엔디티를 삭제 + likeCount 감소
        좋아요가 안 눌러져있음 == Like엔디티 없음 -> (좋아요 선택) -- Like 엔디티 추가 + likeCount 증가

        변경된 likeCount를 Post 엔디티에 반영하고 저장
     */

    public boolean toggleLike(Long postId, Long userId){
        Optional<Like> like = likeRepository.findByPostIdAndUserId(postId, userId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("post not found with postId : " + postId));
        Optional<User> user = userRepository.findById(userId);

        if (like.isPresent()){ // 이미 좋아요가 눌러져있음 -> 좋아요 취소시키기
            likeRepository.delete(like.get()); // like 엔디티 삭제

            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);

            return false; // 좋아요 취소
        } else {
            // 좋아요가 안 눌러져있음 -> 좋아요 상태로 만들기
            Like newLike = new Like();
            newLike.setPost(post);
            newLike.setUser(user.get()); // like -- post, like -- user 연관관계 세팅 완
            likeRepository.save(newLike); // DB에 저장 -- likeEntity

            post.setLikeCount(post.getLikeCount() + 1); // likeCount 올리기
            postRepository.save(post);

            return true; // 좋아요 누른 상태입니다
        }
    }
}
