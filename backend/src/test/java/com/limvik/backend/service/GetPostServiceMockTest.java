package com.limvik.backend.service;

import com.limvik.backend.domain.Post;
import com.limvik.backend.domain.Skill;
import com.limvik.backend.repository.PositionSkillRepository;
import com.limvik.backend.repository.PostRepository;
import com.limvik.backend.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetPostServiceMockTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PositionSkillRepository positionSkillRepository;

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void getAllPost() {
        when(postRepository.findAll()).thenReturn(List.of(Post.builder().id(1L).build(), Post.builder().id(2L).build()));

        assertThat(postService.getPosts().size()).isEqualTo(2);
        assertThat(postService.getPosts().get(1).getId()).isEqualTo(1L);
    }

    @Test
    void getAllPostByKeyword() {
        when(skillRepository.findByName("java")).thenReturn(Optional.of(new Skill(1L, "java", null)));
        when(positionSkillRepository.findPostIdsBySkillId(1L)).thenReturn(Set.of(1L, 2L, 3L));
        when(postRepository.findAllById(Set.of(1L, 2L, 3L))).thenReturn(
                List.of(Post.builder().id(1L).build(), Post.builder().id(2L).build(), Post.builder().id(3L).build()));
        when(postRepository.search("java")).thenReturn(List.of(Post.builder().id(1L).build()));

        assertThat(postService.getPostsByKeyword("java").size()).isEqualTo(3);
        assertThat(postService.getPostsByKeyword("java").get(2).getId()).isEqualTo(1L);
    }

}
