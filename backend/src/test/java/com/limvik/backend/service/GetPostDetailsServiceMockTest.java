package com.limvik.backend.service;

import com.limvik.backend.domain.*;
import com.limvik.backend.exception.PostNotFoundException;
import com.limvik.backend.repository.PositionSkillRepository;
import com.limvik.backend.repository.PostRepository;
import com.limvik.backend.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetPostDetailsServiceMockTest {

    @Mock
    PostRepository postRepository;

    @Mock
    PositionSkillRepository positionSkillRepository;

    @Mock
    SkillRepository skillRepository;

    @InjectMocks
    PostService postService;

    @Test
    void getPostDetails() {

        var targetPostId = 1L;
        var dummyPost = Post.builder().id(targetPostId).build();
        var positionName = "백엔드 주니어 개발자";
        var jobDescription = "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은 java, ...";
        var reward = 1500000L;
        var targetCompanyId = 1L;
        var targetCompanyName = "(주)원티드랩";
        var company = Company.builder().id(targetCompanyId).name(targetCompanyName).build();
        var skill1 = new Skill(2L, "spring");
        var skill2 = new Skill(3L, "javascript");
        var skills = Set.of(new PositionSkill(dummyPost, skill1), new PositionSkill(dummyPost, skill2));
        var streetAddress = "올림픽로 300, 롯데월드타워 35층";
        var cityAddress = "송파구";
        var stateAddress = "서울특별시";
        var address = Address.builder()
                .postId(targetPostId).street(streetAddress).city(cityAddress).state(stateAddress)
                .build();

        var post = Post.builder()
                .id(targetPostId)
                .company(company)
                .address(address)
                .positionName(positionName)
                .jobDescription(jobDescription)
                .reward(reward)
                .positionSkills(skills)
                .build();

        when(postRepository.findById(targetPostId)).thenReturn(Optional.of(post));
        when(positionSkillRepository.findAllByPostId(targetPostId)).thenReturn(skills);

        var returnedPost = postService.getPost(targetPostId);

        assertAll("post",
                () -> assertAll("contents",
                        () -> assertThat(returnedPost.getId()).isEqualTo(targetPostId),
                        () -> assertThat(returnedPost.getJobDescription()).isEqualTo(jobDescription),
                        () -> assertThat(returnedPost.getPositionName()).isEqualTo(positionName)),
                () -> assertAll("company",
                        () -> assertThat(returnedPost.getCompany().getId()).isEqualTo(targetCompanyId),
                        () -> assertThat(returnedPost.getCompany().getName()).isEqualTo(targetCompanyName)),
                () -> assertAll("address",
                        () -> assertThat(returnedPost.getAddress().getPostId()).isEqualTo(targetPostId),
                        () -> assertThat(returnedPost.getAddress().getStreet()).isEqualTo(streetAddress),
                        () -> assertThat(returnedPost.getAddress().getCity()).isEqualTo(cityAddress),
                        () -> assertThat(returnedPost.getAddress().getState()).isEqualTo(stateAddress)),
                () -> assertAll("positionSkills",
                        () -> assertThat(returnedPost.getPositionSkills().size())
                                .isEqualTo(skills.size()),
                        () -> assertThat(returnedPost.getPositionSkills().containsAll(skills))
                                .isEqualTo(true)));
    }

    @Test
    void getNonExistPostAndThrowException() {
        var targetPostId = 99L;

        when(postRepository.findById(targetPostId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPost(targetPostId))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage(new PostNotFoundException(targetPostId).getMessage());
    }

}
