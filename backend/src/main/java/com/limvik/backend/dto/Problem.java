package com.limvik.backend.dto;

public record Problem(
        String title,
        Integer status,
        String[] details
) {
}
