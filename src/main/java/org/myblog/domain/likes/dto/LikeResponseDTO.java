package org.myblog.domain.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeResponseDTO {
    private boolean liked; // true -- 좋아요 누른 상태 , false -- 좋아요 취소한 상태
    private int likeCount; // 특정 post의 좋아요 누른 개수
}
