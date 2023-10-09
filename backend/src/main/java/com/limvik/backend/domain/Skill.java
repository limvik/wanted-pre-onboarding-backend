package com.limvik.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@EqualsAndHashCode(of = "id")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "skill", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PositionSkill> positionSkills;

    public Skill(String name) {
        this.name = name;
    }
}
