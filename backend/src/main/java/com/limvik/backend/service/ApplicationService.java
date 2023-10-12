package com.limvik.backend.service;

import com.limvik.backend.domain.Application;
import com.limvik.backend.domain.Status;
import com.limvik.backend.exception.ApplicationConflictException;
import com.limvik.backend.exception.PostNotFoundException;
import com.limvik.backend.repository.ApplicationRepository;
import com.limvik.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final PostRepository postRepository;

    public Application saveApplication(Application application) {
        validateInfo(application);
        var now = Instant.now();
        application.setAppliedAt(now);
        application.setUpdatedAt(now);
        application.setStatus(Status.builder().id(1L).build());
        return applicationRepository.save(application);
    }

    private void validateInfo(Application application) {
        var postId = application.getIds().getPostId();
        if (!postRepository.existsById(postId)) throw new PostNotFoundException(postId);
        if (applicationRepository.existsById(application.getIds())) throw new ApplicationConflictException(postId);
    }

}
