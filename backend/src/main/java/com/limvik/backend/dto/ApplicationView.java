package com.limvik.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;

public record ApplicationView(
        Long postId,
        Long userId,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Instant appliedAt,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        Instant updatedAt,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String status
) {
        public static ApplicationView applicationRequest(Long postId, Long userId) {
                return new ApplicationView(postId, userId, null, null, null);
        }
}
