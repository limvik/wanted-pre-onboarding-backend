package com.limvik.backend.controller;

import com.limvik.backend.domain.*;
import com.limvik.backend.exception.PostNotFoundException;
import com.limvik.backend.service.PostService;
import com.limvik.backend.service.SkillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PostController.class)
public class ModifyPostControllerMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    SkillService skillService;

    String body;

    @BeforeEach
    void setUp() {
        body = """
                {
                   "address": {
                     "street": "강릉대로 33",
                     "city": "강릉시",
                     "state": "강원특별자치도"
                   },
                   "positionName": "백엔드 슈퍼주니어 개발자",
                   "reward": 100000000,
                   "skills": [
                     {
                       "name": "spring"
                     },
                     {
                       "name": "javascript"
                     }
                   ],
                   "jobDescription": "원티드랩에서 백엔드 슈퍼주니어 개발자를 채용합니다. 자격요건은 java, ..."
                }
                """;
    }

    @Test
    void requestModifyPostAndReturnModifiedPost() throws Exception {

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

        var modifyPost = Post.builder()
                .id(targetPostId)
                .company(company)
                .address(address)
                .positionName(positionName)
                .jobDescription(jobDescription)
                .reward(reward)
                .build();

        var returnedPost = Post.builder()
                .id(targetPostId)
                .company(company)
                .address(address)
                .positionName(positionName)
                .jobDescription(jobDescription)
                .reward(reward)
                .positionSkills(modifiedSkills)
                .build();

        List<Long> otherPosts = new ArrayList<>();
        otherPosts.add(1L);
        otherPosts.add(2L);
        otherPosts.add(5L);

        given(skillService.getSkillByName(skill1.getName())).willReturn(skill1);
        given(skillService.getSkillByName(skill2.getName())).willReturn(skill2);
        given(postService.modifyPost(modifyPost, List.of(skill1, skill2))).willReturn(returnedPost);
        given(skillService.getSkillsByPosition(returnedPost.getPositionSkills())).willReturn(List.of(skill1, skill2));
        given(postService.getPostIdsByCompanyId(targetCompanyId)).willReturn(otherPosts);

        mockMvc.perform(patch("/api/v1/posts/" + targetPostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("Location"))
                .andExpect(content().string(containsString(positionName)))
                .andExpect(content().string(containsString(cityAddress)))
                .andExpect(jsonPath("$.['skills'].length()").value(2))
                .andExpect(jsonPath("$.['otherPostsByCompany'][2]").doesNotExist());

    }

    @Test
    void requestModifyPostAndReturn404NotFound() throws Exception {

        var targetPostId = 9999L;
        given(postService.modifyPost(any(), any())).willThrow(new PostNotFoundException(targetPostId));

        mockMvc.perform(patch("/api/v1/posts/" + targetPostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("입력하신 채용공고의 id = " + targetPostId + "는 존재하지 않습니다.")));
    }

    @Test
    void requestModifyPostAndReturn422UnprocessableEntity() throws Exception {

        var bodyWithEmptyPositionName = """
                {
                   "address": {
                     "street": "강릉대로 33",
                     "city": "강원도",
                     "state": "강원특별자치도"
                   },
                   "positionName": "백엔드 슈퍼주니어 개발자",
                   "reward": 1500000,
                   "skills": [
                     {
                       "name": "Java"
                     }
                   ],
                   "jobDescription": ""
                }
                """;

        mockMvc.perform(patch("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyWithEmptyPositionName))
                .andExpect(status().isUnprocessableEntity());

    }
}
