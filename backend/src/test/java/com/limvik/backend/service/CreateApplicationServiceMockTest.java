package com.limvik.backend.service;

import com.limvik.backend.domain.*;
import com.limvik.backend.exception.ApplicationConflictException;
import com.limvik.backend.exception.PostNotFoundException;
import com.limvik.backend.repository.ApplicationRepository;
import com.limvik.backend.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateApplicationServiceMockTest {

    @Mock
    PostRepository postRepository;

    @Mock
    ApplicationRepository applicationRepository;

    @InjectMocks
    ApplicationService applicationService;

    @Test
    void saveApplicationAndReturnApplicationWithAdditionalInfo() {

        var postId = 1L;
        var userId = 2L;
        var now = Instant.now();
        var statusName = "서류접수";
        var applicationKey = new ApplicationKey(postId, userId);

        var savedApplication = Application.builder()
                .ids(applicationKey)
                .post(Post.builder().id(postId).build())
                .user(User.builder().id(userId).build())
                .status(Status.builder().id(1L).name(statusName).build())
                .appliedAt(now)
                .updatedAt(now)
                .build();

        var application = Application.builder()
                .ids(applicationKey)
                .post(Post.builder().id(postId).build())
                .user(User.builder().id(userId).build())
                .build();

        when(postRepository.existsById(postId)).thenReturn(true);
        when(applicationRepository.existsById(applicationKey)).thenReturn(false);
        when(applicationRepository.save(application)).thenReturn(savedApplication);
        var returnedApplication = applicationService.saveApplication(application);

        assertThat(returnedApplication.getIds().getPostId()).isEqualTo(savedApplication.getIds().getPostId());
        assertThat(returnedApplication.getIds().getUserId()).isEqualTo(savedApplication.getIds().getUserId());
        assertThat(returnedApplication.getAppliedAt().toString()).isEqualTo(savedApplication.getAppliedAt().toString());
        assertThat(returnedApplication.getUpdatedAt().toString()).isEqualTo(savedApplication.getUpdatedAt().toString());
        assertThat(returnedApplication.getStatus().getName()).isEqualTo(savedApplication.getStatus().getName());
    }

    @Test
    void saveApplicationAndThrowPostNotFoundException() {

        var postId = 99999L;
        var userId = 2L;
        var dummyApplication = Application.builder()
                .ids(new ApplicationKey(postId, userId))
                .build();

        when(postRepository.existsById(postId)).thenReturn(false);

        assertThatThrownBy(() -> applicationService.saveApplication(dummyApplication))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage(new PostNotFoundException(postId).getMessage());
    }

    @Test
    void saveApplicationAndThrowApplicationConflictException() {

        var postId = 1L;
        var userId = 2L;
        var applicationKey = new ApplicationKey(postId, userId);
        var dummyApplication = Application.builder()
                .ids(applicationKey)
                .build();

        when(postRepository.existsById(postId)).thenReturn(true);
        when(applicationRepository.existsById(applicationKey)).thenReturn(true);

        assertThatThrownBy(() -> applicationService.saveApplication(dummyApplication))
                .isInstanceOf(ApplicationConflictException.class)
                .hasMessage(new ApplicationConflictException(postId).getMessage());

    }

}
