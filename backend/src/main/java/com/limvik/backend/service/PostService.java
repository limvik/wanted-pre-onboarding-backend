package com.limvik.backend.service;

import com.limvik.backend.domain.Post;
import com.limvik.backend.repository.PositionSkillRepository;
import com.limvik.backend.repository.PostRepository;
import com.limvik.backend.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final PositionSkillRepository positionSkillRepository;
    private final SkillRepository skillRepository;

    public List<Post> getPosts() {
        return postRepository.findAll().stream().sorted((o1, o2) -> Long.compare(o2.getId(), o1.getId())).toList();
    }

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

}
