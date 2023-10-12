package com.limvik.backend.controller;

import com.limvik.backend.domain.*;
import com.limvik.backend.exception.ApplicationConflictException;
import com.limvik.backend.exception.PostNotFoundException;
import com.limvik.backend.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ApplicationController.class)
public class CreateApplicationControllerMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ApplicationService applicationService;

    String body;

    @BeforeEach
    void init() {
        body = """
                {
                  "postId": 12,
                  "userId": 42
                }
                """;
    }

    @Test
    void requestCreateApplicationAndReturnSavedApplication() throws Exception {

        var postId = 12L;
        var userId = 42L;
        var now = Instant.now();
        var statusName = "서류접수";
        var applicationKey = new ApplicationKey(postId, userId);

        var returnedApplication = Application.builder()
                .ids(applicationKey)
                .post(Post.builder().id(postId).build())
                .user(User.builder().id(userId).build())
                .status(Status.builder().id(1L).name(statusName).build())
                .appliedAt(now)
                .updatedAt(now)
                .build();

        given(applicationService.saveApplication(Application.builder().ids(applicationKey).build()))
                .willReturn(returnedApplication);

        mockMvc.perform(post("/api/v1/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.['postId']").value(postId))
                .andExpect(jsonPath("$.['userId']").value(userId))
                .andExpect(jsonPath("$.['appliedAt']").value(now.toString()))
                .andExpect(jsonPath("$.['updatedAt']").value(now.toString()))
                .andExpect(jsonPath("$.['status']").value(statusName));
    }

    @Test
    void requestCreateApplicationAndReturn404NotFound() throws Exception {

        var body = """
                {
                  "postId": 99999,
                  "userId": 42
                }
                """;

        var postId = 99999L;
        given(applicationService.saveApplication(any()))
                .willThrow(new PostNotFoundException(postId));

        mockMvc.perform(post("/api/v1/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(new PostNotFoundException(postId).getMessage())));

    }

    @Test
    void requestCreateApplicationAndReturn409Conflict() throws Exception {

        var postId = 12L;
        given(applicationService.saveApplication(any()))
                .willThrow(new ApplicationConflictException(postId));

        mockMvc.perform(post("/api/v1/applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString(new ApplicationConflictException(postId).getMessage())));

    }
}
