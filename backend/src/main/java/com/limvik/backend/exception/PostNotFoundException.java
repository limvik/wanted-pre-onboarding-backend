package com.limvik.backend.exception;

public class PostNotFoundException extends RuntimeException {

    private static final String messageTemplate = "입력하신 채용공고의 id = %d 는 존재하지 않습니다.";

    public PostNotFoundException(Long postId) {
        super(messageTemplate.formatted(postId));
    }
}
