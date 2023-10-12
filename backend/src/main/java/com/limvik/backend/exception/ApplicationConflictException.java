package com.limvik.backend.exception;

public class ApplicationConflictException extends RuntimeException {

    private static final String messageTemplate = "해당 채용공고(id = %d)에는 이미 지원하여 전형이 진행중입니다.";

    public ApplicationConflictException(Long postId) {
        super(messageTemplate.formatted(postId));
    }

}
