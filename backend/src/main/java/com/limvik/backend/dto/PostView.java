package com.limvik.backend.dto;

public record PostView(
        Long id,
        CompanyView company,
        AddressView address,
        String positionName,
        Long reward,
        SkillView[] skills
) { }
