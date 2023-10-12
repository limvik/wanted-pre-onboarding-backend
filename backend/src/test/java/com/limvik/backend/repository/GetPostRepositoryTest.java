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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DataConfig.class)
@ActiveProfiles("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GetPostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    PositionSkillRepository positionSkillRepository;

    @Test
    void findAllPosts() {
        Iterable<Post> posts = postRepository.findAll();

        assertThat(StreamSupport.stream(posts.spliterator(), true)
                .filter(post -> post.getPositionName().equals("백엔드 주니어 개발자") ||
                                post.getPositionName().equals("프론트 주니어 개발자"))
                .collect(Collectors.toList())).hasSize(3);
    }

    @Test
    void findAllPostByKeywordInPositionNameAndJobDescription() {
        String keyword = "react";
        Long reactSkillId = skillRepository.findByName(keyword).get().getId();
        List<Post> posts1 = postRepository.search(keyword);
        assertThat(posts1).hasSize(1);
        List<Post> posts2 = postRepository.findAllById(positionSkillRepository.findPostIdsBySkillId(reactSkillId));
        assertThat(posts2).hasSize(0);
    }

    @Test
    void findAllPostByKeywordInSkill() {
        String keyword = "spring";
        Long SpringSkillId = skillRepository.findByName(keyword).get().getId();
        List<Post> posts1 = postRepository.search(keyword);
        assertThat(posts1).hasSize(0);
        List<Post> posts2 = postRepository.findAllById(positionSkillRepository.findPostIdsBySkillId(SpringSkillId));
        assertThat(posts2).hasSize(1);
    }

}
