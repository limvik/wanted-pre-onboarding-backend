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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ModifyPostServiceMockTest {

    @Mock
    PostRepository postRepository;

    @Mock
    PositionSkillRepository positionSkillRepository;

    @Mock
    SkillRepository skillRepository;

    @InjectMocks
    PostService postService;

    @Test
    void modifyPostAndReturnPost() {

        var targetPostId = 1L;
        var dummyPost = Post.builder().id(targetPostId).build();
        var positionName = "백엔드 슈퍼주니어 개발자";
        var jobDescription = "원티드랩에서 백엔드 슈퍼주니어 개발자를 채용합니다. 자격요건은 java, ...";
        var reward = 100000000L;
        var targetCompanyId = 1L;
        var targetCompanyName = "(주)원티드랩";
        var company = Company.builder().id(targetCompanyId).name(targetCompanyName).build();
        var skill1 = new Skill(2L, "spring");
        var skill2 = new Skill(3L, "javascript");
        var modifiedSkills = Set.of(new PositionSkill(dummyPost, skill1), new PositionSkill(dummyPost, skill2));
        var streetAddress = "강릉대로 33";
        var cityAddress = "강릉시";
        var stateAddress = "강원특별자치도";
        var address = Address.builder()
                .postId(targetPostId).street(streetAddress).city(cityAddress).state(stateAddress)
                .build();

        var modifiedPost = Post.builder()
                .id(targetPostId)
                .positionName(positionName)
                .jobDescription(jobDescription)
                .reward(reward)
                .company(company)
                .address(address)
                .positionSkills(modifiedSkills)
                .build();
        when(postRepository.findById(any())).thenReturn(Optional.of(modifiedPost));
        when(postRepository.save(modifiedPost)).thenReturn(modifiedPost);
        when(positionSkillRepository.findAllByPostId(modifiedPost.getId())).thenReturn(modifiedSkills);

        var returnedPost = postService.modifyPost(modifiedPost, List.of(skill1, skill2));
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
                                .isEqualTo(modifiedPost.getPositionSkills().size()),
                        () -> assertThat(returnedPost.getPositionSkills().containsAll(modifiedSkills))
                                .isEqualTo(true)));

    }

    @Test
    void modifyNonExistPostAndThrowException() {
        var targetPostId = 99L;
        var post = Post.builder().id(targetPostId).build();
        var skill = new Skill(2L, "spring");

        when(postRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.modifyPost(post, List.of(skill)))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage("입력하신 채용공고의 id = " + post.getId() + "는 존재하지 않습니다.");
    }

}
