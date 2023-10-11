package com.limvik.backend.exception;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long postId) {
        super("입력하신 채용공고의 id = " + postId + "는 존재하지 않습니다.");
    }
}
