package com.limvik.backend.service;

import com.limvik.backend.domain.PositionSkill;
import com.limvik.backend.domain.Skill;
import com.limvik.backend.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public List<Skill> getSkillsByPosition(Set<PositionSkill> positionSkills) {
        List<Long> skillIds = new ArrayList<>();
        for (var positionSkillInfo : positionSkills) {
            skillIds.add(positionSkillInfo.getSkill().getId());
        }
        return skillRepository.findAllById(skillIds);
    }

}
