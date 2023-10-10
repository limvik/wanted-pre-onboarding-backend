package com.limvik.backend.exception;

public class PostNotValidException extends RuntimeException {

    public PostNotValidException() {
        super("채용포지션(positionName) 또는 채용내용(jobDescription)이 작성되지 않았습니다. 확인해 주세요.");
    }

}
