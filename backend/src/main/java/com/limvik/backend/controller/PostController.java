package com.limvik.backend.controller;

import com.limvik.backend.domain.Address;
import com.limvik.backend.domain.Company;
import com.limvik.backend.domain.Post;
import com.limvik.backend.domain.Skill;
import com.limvik.backend.dto.*;
import com.limvik.backend.service.PostService;
import com.limvik.backend.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<List<PostView>> returnAllPosts(@RequestParam(required = false) String search) {
        List<Post> posts = getAllPosts(search);
        return ResponseEntity.ok(mapPostListToPostViewList(posts));
    }

    private List<Post> getAllPosts(String keyword) {
        List<Post> posts;

        if (keyword == null || keyword.isBlank())
            posts = postService.getPosts();
        else
            posts = postService.getPostsByKeyword(keyword);

        return posts;
    }

    private List<PostView> mapPostListToPostViewList(List<Post> posts) {
        List<PostView> postViews = new ArrayList<>();
        for (var post : posts) {
            postViews.add(mapPostToPostView().map(post));
        }
        return postViews;
    }

    private DtoMapper<Post, PostView> mapPostToPostView() {
        return (post) -> new PostView(
                post.getId(),
                mapCompanyToCompanyView().map(post.getCompany()),
                mapAddressToAddressView().map(post.getAddress()),
                post.getPositionName(),
                post.getReward(),
                getSkillViews(post));
    }

    private DtoMapper<Company, CompanyView> mapCompanyToCompanyView() {
        return (company) -> new CompanyView(company.getId(), company.getName());
    }

    private DtoMapper<Address, AddressView> mapAddressToAddressView() {
        return (address) -> new AddressView(address.getStreet(), address.getCity(), address.getState());
    }

    private SkillView[] getSkillViews(Post post) {
        List<Skill> skills = skillService.getSkillsByPosition(post.getPositionSkills());
        SkillView[] skillViews = new SkillView[skills.size()];
        for(int i = 0; i < skillViews.length; i++) {
            skillViews[i] = mapSkillToSkillView().map(skills.get(i));
        }
        return  skillViews;
    }

    private DtoMapper<Skill, SkillView> mapSkillToSkillView() {
        return (skill) -> new SkillView(skill.getName());
    }
}
