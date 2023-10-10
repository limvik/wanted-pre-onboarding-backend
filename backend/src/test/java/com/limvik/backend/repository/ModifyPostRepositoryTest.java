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

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DataConfig.class)
@ActiveProfiles("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ModifyPostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    PositionSkillRepository positionSkillRepository;

    @Autowired
    AddressRepository addressRepository;

    @Test
    void modifyPost() {

        String positionName = "백엔드 슈퍼주니어 개발자";
        String jobDescription = "원티드랩에서 백엔드 슈퍼주니어 개발자를 채용합니다. 자격요건은 java, ...";
        Long reward = 1000000000L;
        Company company = companyRepository.findById(1L).get();
        String streetAddress = "강릉대로 33";
        String cityAddress = "강릉시";
        String stateAddress = "강원특별자치도";
        var dummyPost = Post.builder().id(1L).build();
        Address address = Address.builder()
                .postId(1L).street(streetAddress).city(cityAddress).state(stateAddress)
                .build();
        var skill1 = skillRepository.findByName("java").get();
        var skill2 = skillRepository.findByName("react").get();
        var existSkill = skillRepository.findByName("spring").get();
        positionSkillRepository.deleteAllById(Set.of(new PositionSkillKey(dummyPost.getId(), existSkill.getId())));
        var positionSkills = Set.of(new PositionSkill(dummyPost, skill1), new PositionSkill(dummyPost, skill2));

        var post = Post.builder()
                .id(1L)
                .positionName(positionName)
                .jobDescription(jobDescription)
                .reward(reward)
                .company(company)
                .address(address)
                .positionSkills(positionSkills)
                .build();

        var returnedPost = postRepository.save(post);

        var newPost = postRepository.findById(returnedPost.getId()).get();
        assertThat(newPost.getId()).isEqualTo(1L);
        assertThat(newPost.getPositionName()).isEqualTo(positionName);
        assertThat(newPost.getJobDescription()).isEqualTo(jobDescription);
        assertThat(newPost.getReward()).isEqualTo(reward);
        assertThat(newPost.getCompany().getId()).isEqualTo(company.getId());
        assertThat(newPost.getAddress().getPostId()).isEqualTo(address.getPostId());
        assertThat(newPost.getAddress().getStreet()).isEqualTo(streetAddress);
        assertThat(newPost.getAddress().getCity()).isEqualTo(cityAddress);
        assertThat(newPost.getAddress().getState()).isEqualTo(stateAddress);
        assertThat(newPost.getPositionSkills()).hasSize(2);
        assertThat(newPost.getPositionSkills().containsAll(positionSkills)).isTrue();

    }

}
