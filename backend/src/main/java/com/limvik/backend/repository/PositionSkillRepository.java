package com.limvik.backend.repository;

import com.limvik.backend.domain.PositionSkill;
import com.limvik.backend.domain.PositionSkillKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PositionSkillRepository extends JpaRepository<PositionSkill, PositionSkillKey> {

    @Query("SELECT p.ids.postId FROM PositionSkill p WHERE p.ids.skillId = :id")
    Set<Long> findPostIdsBySkillId(@Param("id") Long id);

}
