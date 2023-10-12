package com.limvik.backend.controller;

import com.limvik.backend.domain.Application;
import com.limvik.backend.domain.ApplicationKey;
import com.limvik.backend.domain.Post;
import com.limvik.backend.domain.User;
import com.limvik.backend.dto.ApplicationView;
import com.limvik.backend.dto.DtoMapper;
import com.limvik.backend.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<ApplicationView> createApplication(@RequestBody ApplicationView applicationView) {
        var savedApplication = applicationService.saveApplication(mapApplicationViewToApplication().map(applicationView));
        var returnApplication = mapApplicationToApplicationView().map(savedApplication);
        return ResponseEntity
                .created(URI.create("/api/v1/applications/"+applicationView.postId()))
                .body(returnApplication);
    }

    private DtoMapper<ApplicationView, Application> mapApplicationViewToApplication() {
        return (applicationView) -> Application.builder()
                .ids(new ApplicationKey(applicationView.postId(), applicationView.userId()))
                .user(User.builder().id(applicationView.userId()).build())
                .post(Post.builder().id(applicationView.postId()).build())
                .build();
    }

    private DtoMapper<Application, ApplicationView> mapApplicationToApplicationView() {
        return (application) -> new ApplicationView(
                application.getIds().getPostId(),
                application.getIds().getUserId(),
                application.getAppliedAt(),
                application.getUpdatedAt(),
                application.getStatus().getName()
        );
    }
}
