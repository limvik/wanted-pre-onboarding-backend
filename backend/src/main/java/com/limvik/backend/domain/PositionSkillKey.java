package com.limvik.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PositionSkillKey implements Serializable {

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "skill_id", nullable = false)
    private Long skillId;

}
