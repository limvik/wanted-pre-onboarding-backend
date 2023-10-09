package com.limvik.backend.dto;

public interface DtoMapper<Input, Output> {
    Output map(Input obj);
}
