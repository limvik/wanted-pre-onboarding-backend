package com.limvik.backend.controller;

import com.limvik.backend.domain.*;
import com.limvik.backend.service.PostService;
import com.limvik.backend.service.SkillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
public class GetPostControllerMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    SkillService skillService;

    @Test
    void returnAllPosts() throws Exception {

        var post1 = Post.builder()
                .id(1L)
                .company(Company.builder().name("(주)원티드랩").build())
                .address(Address.builder().street("올림픽로 300, 롯데월드타워 35층").city("송파구").state("서울특별시").build())
                .build();

        var post2 = Post.builder()
                .id(2L)
                .company(Company.builder().name("(주)사람인에이치알").build())
                .address(Address.builder().street("디지털로 34길 43 14층 (구로동, 코오롱싸이언스밸리1차)").city("구로구").state("서울특별시").build())
                .build();

        given(postService.getPosts())
                .willReturn(List.of(post1, post2));

        mockMvc.perform(get("/api/v1/posts"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("(주)원티드랩")))
                .andExpect(content().string(containsString("송파구")))
                .andExpect(content().string(containsString("(주)사람인에이치알")))
                .andExpect(content().string(containsString("구로구")));

    }

    @Test
    void returnAllPostsByKeyword() throws Exception {

        var post1 = Post.builder()
                .id(1L)
                .company(Company.builder().name("(주)원티드랩").build())
                .address(Address.builder().street("올림픽로 300, 롯데월드타워 35층").city("송파구").state("서울특별시").build())
                .positionName("주니어 백엔드 개발자")
                .positionSkills(Set.of(new PositionSkill(new Post(), new Skill("java"))))
                .build();

        var post2 = Post.builder()
                .id(2L)
                .company(Company.builder().name("(주)사람인에이치알").build())
                .address(Address.builder().street("디지털로 34길 43 14층 (구로동, 코오롱싸이언스밸리1차)").city("구로구").state("서울특별시").build())
                .jobDescription("주니어 프론트엔드 개발자를 모집합니다.")
                .build();

        given(postService.getPostsByKeyword("주니어"))
                .willReturn(List.of(post1, post2));

        mockMvc.perform(get("/api/v1/posts?search=주니어"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("(주)원티드랩")))
                .andExpect(content().string(containsString("송파구")))
                .andExpect(content().string(containsString("(주)사람인에이치알")))
                .andExpect(content().string(containsString("구로구")));

        given(postService.getPostsByKeyword("java"))
                .willReturn(List.of(post1));

        mockMvc.perform(get("/api/v1/posts?search=java"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("(주)원티드랩")))
                .andExpect(content().string(containsString("송파구")));

    }
}
