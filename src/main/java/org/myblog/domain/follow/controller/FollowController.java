package org.myblog.domain.follow.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.myblog.domain.follow.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping(value = "/follow")
    @ResponseBody
    public ResponseEntity<?> followUser(@RequestBody FollowRequest followRequest){
        boolean followResult = followService.toggleFollow(followRequest.getUserId(), followRequest.getFollowedUserId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new FollowResponse(followResult));
    }

    @Data
    static class FollowRequest {
        private Long userId; // 현재 로그인한 유저 -- id1
        private Long followedUserId; // 팔로우 대상 -- id2
    }

    @Data
    @AllArgsConstructor
    static class FollowResponse {
        private boolean followResult; // 팔로잉여부 (T - 팔로우, F - 언팔로우)
    }

}
