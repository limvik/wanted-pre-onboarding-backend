package com.limvik.backend.service;

import com.limvik.backend.exception.PostNotFoundException;
import com.limvik.backend.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeletePostServiceMockTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    @Test
    void modifyNonExistPostAndThrowException() {
        var targetPostId = 99L;

        when(postRepository.findById(targetPostId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.deletePost(targetPostId))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage(new PostNotFoundException(targetPostId).getMessage());

    }
}
