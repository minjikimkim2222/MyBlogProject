package org.myblog.domain.follow.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.myblog.domain.follow.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    // /follow/status?userId=${userId}&followedUserId=${followedUserId}
    // 현재 로그인한 사용자가 특정 사용자를 팔로우하고 있는지 확인하는 메서드
    @GetMapping("/follow/status")
    @ResponseBody
    public ResponseEntity<?> checkFollowStatus(@RequestParam Long userId, @RequestParam Long followedUserId){
        boolean followResult = followService.isFollowing(userId, followedUserId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new FollowResponse(followResult)); // 이미 팔로잉했는지 여부 -- T : 팔로우했음, F : 팔로우안했음
    }


    @PostMapping(value = "/follow")
    @ResponseBody
    public ResponseEntity<?> followUser(@RequestBody FollowRequest followRequest){
        boolean followResult = followService.toggleFollow(followRequest.getUserId(), followRequest.getFollowedUserId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new FollowResponse(followResult)); // 팔로잉여부 (T - 팔로우, F - 언팔로우)
    }

    @Data
    static class FollowRequest {
        private Long userId; // 현재 로그인한 유저 -- id1
        private Long followedUserId; // 팔로우 대상 -- id2
    }

    @Data
    @AllArgsConstructor
    static class FollowResponse {
        private boolean followResult;
    }

}
