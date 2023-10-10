package com.limvik.backend.controller;

import com.limvik.backend.domain.Address;
import com.limvik.backend.domain.Company;
import com.limvik.backend.domain.Post;
import com.limvik.backend.domain.Skill;
import com.limvik.backend.dto.*;
import com.limvik.backend.exception.PostNotValidException;
import com.limvik.backend.service.PostService;
import com.limvik.backend.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<PostView> returnCreatedPost(@RequestBody PostView createRequestedPost) {
        validateRequestedPost(createRequestedPost);
        var savedPost = mapPostDetailViewToPost(createRequestedPost);
        var returnedPost = mapPostToPostView().map(savedPost);
        return ResponseEntity.created(URI.create("/api/v1/posts/" + returnedPost.id())).body(returnedPost);
    }

    private void validateRequestedPost(PostView postView) {
        if (!StringUtils.hasText(postView.positionName()) || !StringUtils.hasText(postView.jobDescription()))
            throw new PostNotValidException();
    }

    private List<Post> getAllPosts(String keyword) {
        List<Post> posts;

        if (keyword == null || keyword.isBlank())
            posts = postService.getPosts();
        else
            posts = postService.getPostsByKeyword(keyword);

        return posts;
    }

    private Post mapPostDetailViewToPost(PostView postView) {
        var post = mapPostViewToPost().map(postView);
//        // 로그인 기능을 생략하여 임시로 첫 번째 회사의 정보를 사용
//        post.setCompany(Company.builder().id(1L).name("(주)원티드랩").build());
        return postService.createPost(post, getSkills(postView));
    }

    private List<PostView> mapPostListToPostViewList(List<Post> posts) {
        List<PostView> postViews = new ArrayList<>();
        for (var post : posts) {
            postViews.add(mapPostToPostView().map(post));
        }
        return postViews;
    }

    private DtoMapper<PostView, Post> mapPostViewToPost() {
        return (postView) -> Post.builder()
                .company(mapCompanyViewToCompany().map(postView.company()))
                .address(mapAddressViewToAddress().map(postView.address()))
                .positionName(postView.positionName())
                .reward(postView.reward())
                .jobDescription(postView.jobDescription())
                .build();
    }

    private DtoMapper<Post, PostView> mapPostToPostView() {
        return (post) -> {
            var jobDescription = post.getJobDescription();
            Long[] otherPosts = null;
            if (jobDescription != null) {
                var postIds = postService.getPostIdsByCompanyId(post.getCompany().getId());
                postIds.remove(post.getId());
                otherPosts = postIds.toArray(Long[]::new);
            }
            return new PostView(
                    post.getId(),
                    mapCompanyToCompanyView().map(post.getCompany()),
                    mapAddressToAddressView().map(post.getAddress()),
                    post.getPositionName(),
                    post.getReward(),
                    getSkillViews(post),
                    jobDescription,
                    otherPosts);
        };
    }

    private DtoMapper<Company, CompanyView> mapCompanyToCompanyView() {
        return (company) -> new CompanyView(company.getId(), company.getName());
    }

    private DtoMapper<CompanyView, Company> mapCompanyViewToCompany() {
        return (companyView) -> Company.builder().id(/*companyView.id()*/1L).name(/*companyView.name()*/"(주)원티드랩").build();
    }

    private DtoMapper<Address, AddressView> mapAddressToAddressView() {
        return (address) -> new AddressView(address.getStreet(), address.getCity(), address.getState());
    }

    private DtoMapper<AddressView, Address> mapAddressViewToAddress() {
        return (addressView) -> Address.builder()
                .street(addressView.street())
                .city(addressView.city())
                .state(addressView.state())
                .build();
    }

    private SkillView[] getSkillViews(Post post) {
        List<Skill> skills = skillService.getSkillsByPosition(post.getPositionSkills());
        SkillView[] skillViews = new SkillView[skills.size()];
        for(int i = 0; i < skillViews.length; i++) {
            skillViews[i] = mapSkillToSkillView().map(skills.get(i));
        }
        return skillViews;
    }

    private DtoMapper<Skill, SkillView> mapSkillToSkillView() {
        return (skill) -> new SkillView(skill.getName());
    }

    private List<Skill> getSkills(PostView postView) {
        List<Skill> skills = new ArrayList<>();
        for (var skillView : postView.skills()) {
            skills.add(mapSkillViewToSkill().map(skillView));
        }
        return skills;
    }

    private DtoMapper<SkillView, Skill> mapSkillViewToSkill() {
        return (skillView) -> skillService.getSkillByName(skillView.name());
    }

}
