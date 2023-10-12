package com.limvik.backend.repository;

import com.limvik.backend.config.DataConfig;
import com.limvik.backend.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DataConfig.class)
@ActiveProfiles("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreateApplicationRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    ApplicationRepository applicationRepository;

    @Test
    void saveApplication() {
        var postId = 1L;
        var userId = 1L;
        var now = Instant.now();
        var statusName = "서류접수";
        var applicationKey = new ApplicationKey(postId, userId);
        var application = Application.builder()
                .ids(applicationKey)
                .post(Post.builder().id(postId).build())
                .user(User.builder().id(userId).build())
                .status(Status.builder().id(1L).name(statusName).build())
                .appliedAt(now)
                .updatedAt(now)
                .build();

        assertThat(postRepository.existsById(postId)).isTrue();
        assertThat(applicationRepository.existsById(applicationKey)).isFalse();

        var savedApplication = applicationRepository.save(application);

        assertThat(savedApplication.getIds().getPostId()).isEqualTo(application.getIds().getPostId());
        assertThat(savedApplication.getIds().getUserId()).isEqualTo(application.getIds().getUserId());
        assertThat(savedApplication.getAppliedAt().toString()).isEqualTo(application.getAppliedAt().toString());
        assertThat(savedApplication.getUpdatedAt().toString()).isEqualTo(application.getUpdatedAt().toString());
        assertThat(savedApplication.getStatus().getName()).isEqualTo(application.getStatus().getName());
    }
}

