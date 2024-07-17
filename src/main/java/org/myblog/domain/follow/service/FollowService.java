package org.myblog.domain.follow.service;

import lombok.RequiredArgsConstructor;
import org.myblog.domain.follow.domain.Follow;
import org.myblog.domain.follow.repository.FollowRepository;
import org.myblog.domain.post.repository.PostRepository;
import org.myblog.domain.user.domain.User;
import org.myblog.domain.user.exception.UserNotFoundException;
import org.myblog.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.myblog.domain.follow.domain.Follow.createFollow;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public boolean toggleFollow(Long userId, Long followedUserId){ // userId -- id1, followedUserId -- id2
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found -- followService"));
        User followedUser = userRepository.findById(followedUserId).orElseThrow(() -> new UserNotFoundException("followed user not found -- followService"));

        Follow foundFollow = followRepository.findByFollowingUserAndFollowedUser(user, followedUser); // -- 트랜잭션 안에서 영속화된 상태!!

        if (foundFollow != null){ // 팔로우한 상태인데, 팔로우 버튼 또누름 -> 언팔로우
            followRepository.delete(foundFollow);

            return false; // 언팔로우

        } else { // 팔로우
            Follow follow = createFollow(user, followedUser); // 그저 자바 객체를 만들었을 뿐, 영속화 X
            followRepository.save(follow); // 영속화시킴. 트랜잭션 끝나고, 모든 쿼리문이 올라감

            return true; // 팔로우
        }

    }

    @Transactional(readOnly = true)
    public boolean isFollowing(Long userId, Long followedUserId){
        return followRepository.existsByFollowingUserIdAndFollowedUserId(userId, followedUserId);
    }

}
