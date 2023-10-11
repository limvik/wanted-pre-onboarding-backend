package com.limvik.backend.controller;

import com.limvik.backend.exception.PostNotFoundException;
import com.limvik.backend.service.PostService;
import com.limvik.backend.service.SkillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(controllers = PostController.class)
public class DeletePostControllerMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @MockBean
    SkillService skillService;

    @Test
    void requestDeletePostAndReturn204WithNothing() throws Exception {

        var targetPostId = 1L;

        doAnswer(invocation -> null)
                .when(postService).deletePost(anyLong());

        mockMvc.perform(delete("/api/v1/posts/" + targetPostId))
                .andExpect(status().isNoContent());

    }

    @Test
    void requestDeletePostAndReturn404() throws Exception {

        var targetPostId = 999L;
        doThrow(new PostNotFoundException(targetPostId))
                .when(postService).deletePost(anyLong());

        mockMvc.perform(delete("/api/v1/posts/" + targetPostId))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(new PostNotFoundException(targetPostId).getMessage())));

    }
}
