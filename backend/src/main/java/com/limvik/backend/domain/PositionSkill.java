package com.limvik.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@EqualsAndHashCode(of = {"post", "skill"})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "position_skills")
public class PositionSkill {

    @EmbeddedId
    private PositionSkillKey ids = new PositionSkillKey();

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @MapsId("skillId")
    @JoinColumn(name = "skill_id")
    private Skill skill;

    public PositionSkill(Post post, Skill skill) {
        this.post = post;
        this.skill = skill;
    }
}
