package com.limvik.backend.service;

import com.limvik.backend.domain.Address;
import com.limvik.backend.domain.PositionSkill;
import com.limvik.backend.domain.Post;
import com.limvik.backend.domain.Skill;
import com.limvik.backend.repository.AddressRepository;
import com.limvik.backend.repository.PositionSkillRepository;
import com.limvik.backend.repository.PostRepository;
import com.limvik.backend.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PositionSkillRepository positionSkillRepository;
    private final SkillRepository skillRepository;
    private final AddressRepository addressRepository;

    @Transactional(readOnly = true)
    public List<Post> getPosts() {
        return postRepository.findAll().stream().sorted((o1, o2) -> Long.compare(o2.getId(), o1.getId())).toList();
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByKeyword(String keyword) {
        Set<Post> posts = getPostsBySkillName(keyword);
        posts.addAll(postRepository.search(keyword));
        return posts.stream().sorted((o1, o2) -> Long.compare(o2.getId(), o1.getId())).toList();
    }

    private Set<Post> getPostsBySkillName(String name) {
        var skill = skillRepository.findByName(name);
        Set<Post> posts = new HashSet<>();
        if (skill.isPresent()) {
            var postIds = positionSkillRepository.findPostIdsBySkillId(skill.get().getId());
            posts.addAll(postRepository.findAllById(postIds));
        }
        return posts;
    }

    @Transactional(readOnly = true)
    public List<Long> getPostIdsByCompanyId(Long companyId) {
        return postRepository.findAllIdByCompanyId(companyId);
    }

    @Transactional
    public Post createPost(Post post, List<Skill> skills) {
        return savePost(post, skills);
    }

    private Post savePost(Post post, List<Skill> skills) {
        var address = post.getAddress();
        var savedPost = savePost(post);
        savedPost.setAddress(saveAddress(savedPost, address));
        savedPost.setPositionSkills(savePositionSkills(savedPost, skills));
        return savedPost;
    }

    private Post savePost(Post post) {
        post.setAddress(null);
        return postRepository.save(post);
    }

    private Address saveAddress(Post savedPost, Address address) {
        address.setPost(savedPost);
        return addressRepository.save(address);
    }

    private Set<PositionSkill> savePositionSkills(Post post, List<Skill> skills) {
        Set<PositionSkill> positionSkills = new HashSet<>();
        for (var skill : skills) {
            positionSkills.add(new PositionSkill(post, skill));
        }
        var returnedPositionSkill = positionSkillRepository.saveAll(positionSkills);
        positionSkills.clear();
        positionSkills.addAll(returnedPositionSkill);
        return positionSkills;
    }

}
