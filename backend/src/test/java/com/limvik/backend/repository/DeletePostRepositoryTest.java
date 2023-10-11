package com.limvik.backend.repository;

import com.limvik.backend.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DataConfig.class)
@ActiveProfiles("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeletePostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Test
    void deletePost() {
        var targetPostId = 1L;
        assertThat(postRepository.findById(targetPostId).isPresent()).isTrue();

        var countTotalPosts = postRepository.count();
        postRepository.deleteById(targetPostId);
        assertThat(countTotalPosts - postRepository.count()).isEqualTo(1);
    }

}
