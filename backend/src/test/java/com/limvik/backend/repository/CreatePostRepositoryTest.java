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
public class CreatePostRepositoryTest {

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
    void savePost() {
        postRepository.deleteAll();
        positionSkillRepository.deleteAll();

        String positionName = "백엔드 주니어 개발자";
        String jobDescription = "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은 java, ...";
        Long reward = 1500000L;
        Company company = companyRepository.findById(1L).get();

        var post = Post.builder()
                .positionName(positionName)
                .jobDescription(jobDescription)
                .reward(reward)
                .company(company)
                .build();

        var returnedPost = postRepository.save(post);
        var skill = skillRepository.findByName("java").get();
        var returnedPositionSkills = positionSkillRepository.saveAll(Set.of(new PositionSkill(returnedPost, skill)));

        String streetAddress = "올림픽로 300, 롯데월드타워 35층";
        String cityAddress = "송파구";
        String stateAddress = "서울특별시";
        Address address = Address.builder()
                .post(returnedPost).street(streetAddress).city(cityAddress).state(stateAddress)
                .build();
        var returnedAddress = addressRepository.save(address);

        var newPost = postRepository.findById(returnedPost.getId()).get();
        assertThat(newPost.getPositionName()).isEqualTo(positionName);
        assertThat(newPost.getJobDescription()).isEqualTo(jobDescription);
        assertThat(newPost.getReward()).isEqualTo(reward);
        assertThat(newPost.getCompany().getId()).isEqualTo(1L);
        assertThat(returnedAddress.getPostId()).isEqualTo(returnedPost.getId());
        assertThat(returnedAddress.getStreet()).isEqualTo(streetAddress);
        assertThat(returnedAddress.getCity()).isEqualTo(cityAddress);
        assertThat(returnedAddress.getState()).isEqualTo(stateAddress);
        assertThat(returnedPositionSkills).hasSize(1);

    }

}
