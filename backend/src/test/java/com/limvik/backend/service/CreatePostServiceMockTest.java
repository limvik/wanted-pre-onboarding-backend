package com.limvik.backend.service;

import com.limvik.backend.domain.*;
import com.limvik.backend.repository.AddressRepository;
import com.limvik.backend.repository.PositionSkillRepository;
import com.limvik.backend.repository.PostRepository;
import com.limvik.backend.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreatePostServiceMockTest {

    @Mock
    PostRepository postRepository;

    @Mock
    PositionSkillRepository positionSkillRepository;

    @Mock
    SkillRepository skillRepository;

    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    PostService postService;

    @Test
    void savePostAndReturnPost() {

        var positionName = "백엔드 주니어 개발자";
        var jobDescription = "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은 java, ...";
        var reward = 1500000L;
        var company = Company.builder().id(1L).name("(주)원티드랩").build();
        var skill = new Skill(1L, "java");
        var streetAddress = "올림픽로 300, 롯데월드타워 35층";
        var cityAddress = "송파구";
        var stateAddress = "서울특별시";
        var address = Address.builder()
                .street(streetAddress).city(cityAddress).state(stateAddress)
                .build();

        var post = Post.builder()
                .positionName(positionName)
                .jobDescription(jobDescription)
                .reward(reward)
                .company(company)
                .address(address)
                .build();

        var savedPost = Post.builder()
                .id(1L)
                .positionName(positionName)
                .jobDescription(jobDescription)
                .reward(reward)
                .company(company)
                .build();

        var addressWithSavedPost = Address.builder()
                .post(savedPost).street(streetAddress).city(cityAddress).state(stateAddress)
                .build();

        var returnedAddress = Address.builder().postId(1L)
                .post(savedPost).street(streetAddress).city(cityAddress).state(stateAddress)
                .build();

        var positionSkill = new PositionSkill();
        positionSkill.setPost(savedPost);
        positionSkill.setSkill(skill);

        var returnedPositionSkill = new PositionSkill();
        returnedPositionSkill.setPost(savedPost);
        returnedPositionSkill.setSkill(skill);
        returnedPositionSkill.setIds(new PositionSkillKey(savedPost.getId(), skill.getId()));

        var expectedPost = Post.builder()
                .id(1L)
                .positionName(positionName)
                .jobDescription(jobDescription)
                .reward(reward)
                .company(company)
                .address(returnedAddress)
                .positionSkills(Set.of(returnedPositionSkill))
                .build();

        when(postRepository.save(post)).thenReturn(savedPost);
        when(addressRepository.save(addressWithSavedPost)).thenReturn(returnedAddress);
        when(positionSkillRepository.saveAll(Set.of(positionSkill))).thenReturn(List.of(returnedPositionSkill));

        var returnedPost = postService.createPost(post, List.of(skill));

        assertThat(returnedPost.getId()).isEqualTo(expectedPost.getId());
        assertThat(returnedPost.getPositionName()).isEqualTo(expectedPost.getPositionName());
        assertThat(returnedPost.getCompany().getId()).isEqualTo(expectedPost.getCompany().getId());
        assertThat(returnedPost.getCompany().getName()).isEqualTo(expectedPost.getCompany().getName());
        assertThat(returnedPost.getAddress().getPostId()).isEqualTo(expectedPost.getAddress().getPostId());
        assertThat(returnedPost.getAddress().getStreet()).isEqualTo(expectedPost.getAddress().getStreet());
        assertThat(returnedPost.getAddress().getCity()).isEqualTo(expectedPost.getAddress().getCity());
        assertThat(returnedPost.getAddress().getState()).isEqualTo(expectedPost.getAddress().getState());
        assertThat(returnedPost.getJobDescription()).isEqualTo(expectedPost.getJobDescription());
        assertThat(returnedPost.getPositionSkills().size()).isEqualTo(expectedPost.getPositionSkills().size());
        assertThat(returnedPost.getPositionSkills().contains(returnedPositionSkill)).isEqualTo(true);
    }
}
