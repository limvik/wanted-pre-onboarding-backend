package com.limvik.backend.controller;

import com.limvik.backend.domain.*;
import com.limvik.backend.exception.PostNotFoundException;
import com.limvik.backend.service.PostService;
import com.limvik.backend.service.SkillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PostController.class)
public class GetPostDetailsControllerMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    SkillService skillService;

    @Test
    void requestPostDetailsAndReturn200WithPostDetails() throws Exception {

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

        var returnedPost = Post.builder()
                .id(targetPostId)
                .company(company)
                .address(address)
                .positionName(positionName)
                .jobDescription(jobDescription)
                .reward(reward)
                .positionSkills(skills)
                .build();

        List<Long> otherPosts = new ArrayList<>();
        otherPosts.add(1L);
        otherPosts.add(2L);
        otherPosts.add(5L);

        given(postService.getPost(targetPostId)).willReturn(returnedPost);
        given(skillService.getSkillsByPosition(returnedPost.getPositionSkills())).willReturn(List.of(skill1, skill2));
        given(postService.getPostIdsByCompanyId(targetCompanyId)).willReturn(otherPosts);

        mockMvc.perform(get("/api/v1/posts/" + targetPostId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['company']['name']").value(targetCompanyName))
                .andExpect(jsonPath("$.['positionName']").value(positionName))
                .andExpect(jsonPath("$.['address']['street']").value(streetAddress))
                .andExpect(jsonPath("$.['skills'].length()").value(2))
                .andExpect(jsonPath("$.['otherPostsByCompany'][0]").value(2))
                .andExpect(jsonPath("$.['otherPostsByCompany'][1]").value(5));

    }

    @Test
    void requestPostDetailsAndReturn404NotFound() throws Exception {

        var targetPostId = 999L;

        given(postService.getPost(targetPostId)).willThrow(new PostNotFoundException(targetPostId));

        mockMvc.perform(get("/api/v1/posts/" + targetPostId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(new PostNotFoundException(targetPostId).getMessage())));
    }

}
