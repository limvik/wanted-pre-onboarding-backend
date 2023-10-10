package com.limvik.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

public record PostView(
        Long id,
        CompanyView company,
        AddressView address,
        String positionName,
        Long reward,
        SkillView[] skills,
        @JsonInclude(Include.NON_EMPTY)
        String jobDescription,
        @JsonInclude(Include.NON_EMPTY)
        Long[] otherPostsByCompany
) {
    public static PostView postListOf(Long id,
                                      CompanyView company,
                                      AddressView address,
                                      String positionName,
                                      Long reward,
                                      SkillView[] skills) {
        return new PostView(id, company, address, positionName, reward, skills, null, null);
    }
}
