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
public class GetPostDetailsRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PositionSkillRepository positionSkillRepository;

    @Test
    void getPostDetails() {
        var targetPostId = 1L;
        assertThat(postRepository.findById(targetPostId).isPresent()).isTrue();
        assertThat(positionSkillRepository.findSkillIdByPostId(targetPostId).size()).isEqualTo(1);
    }

}
