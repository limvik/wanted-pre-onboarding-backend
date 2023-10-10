package com.limvik.backend.controller;

import com.limvik.backend.domain.*;
import com.limvik.backend.service.PostService;
import com.limvik.backend.service.SkillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
public class CreatePostControllerMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    SkillService skillService;

    @Test
    void requestCreatePostAndReturnSavedPost() throws Exception {

        var body = """
                {
                   "address": {
                     "street": "올림픽로 300, 롯데월드타워 35층",
                     "city": "송파구",
                     "state": "서울특별시"
                   },
                   "positionName": "백엔드 주니어 개발자",
                   "reward": 1500000,
                   "skills": [
                     {
                       "name": "Java"
                     }
                   ],
                   "jobDescription": "원티드랩에서 백엔드 주니어 개발자를 채용합니다."
                }
                """;

        var savePost = Post.builder()
                .company(Company.builder().id(1L).name("(주)원티드랩").build())
                .address(Address.builder().street("올림픽로 300, 롯데월드타워 35층").city("송파구").state("서울특별시").build())
                .positionName("주니어 백엔드 개발자")
                .jobDescription("원티드랩에서 백엔드 주니어 개발자를 채용합니다.")
                .reward(1500000L)
                .build();
        var skill = new Skill(1L, "java");
        var returnedPost = Post.builder()
                .id(5L)
                .company(Company.builder().id(1L).name("(주)원티드랩").build())
                .address(Address.builder().street("올림픽로 300, 롯데월드타워 35층").city("송파구").state("서울특별시").build())
                .positionName("주니어 백엔드 개발자")
                .jobDescription("원티드랩에서 백엔드 주니어 개발자를 채용합니다.")
                .reward(1500000L)
                .positionSkills(Set.of(new PositionSkill(Post.builder().id(5L).build(), skill)))
                .build();

        List<Long> otherPosts = new ArrayList<>();
        otherPosts.add(1L);
        otherPosts.add(2L);
        otherPosts.add(5L);

        given(skillService.getSkillByName(any())).willReturn(skill);
        given(postService.createPost(savePost, List.of(skill))).willReturn(returnedPost);
        given(postService.getPostIdsByCompanyId(1L)).willReturn(otherPosts);


        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().string(containsString("(주)원티드랩")))
                .andExpect(content().string(containsString("채용합니다")))
                .andExpect(jsonPath("$.['otherPostsByCompany'][2]").doesNotExist());

    }

    @Test
    void requestCreatePostAndReturn422() throws Exception {

        var bodyWithEmptyPositionName = """
                {
                   "address": {
                     "street": "올림픽로 300, 롯데월드타워 35층",
                     "city": "송파구",
                     "state": "서울특별시"
                   },
                   "positionName": "",
                   "reward": 1500000,
                   "skills": [
                     {
                       "name": "Java"
                     }
                   ],
                   "jobDescription": "원티드랩에서 백엔드 주니어 개발자를 채용합니다."
                }
                """;

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyWithEmptyPositionName))
                .andExpect(status().isUnprocessableEntity());
    }
}
