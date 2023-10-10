package com.limvik.backend.controller;

import com.limvik.backend.dto.AddressView;
import com.limvik.backend.dto.CompanyView;
import com.limvik.backend.dto.PostView;
import com.limvik.backend.dto.SkillView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class PostDetailsJsonTest {

    @Autowired
    JacksonTester<PostView> json;

    private static PostView postDetails;

    @BeforeAll
    static void init() {
        postDetails = new PostView(
                77L,
                new CompanyView(1L, "(주)원티드랩"),
                new AddressView("올림픽로 300, 롯데월드타워 35층", "송파구", "서울특별시"),
                "백엔드 주니어 개발자",
                1500000L,
                new SkillView[]{new SkillView("Java")},
                "원티드랩에서 백엔드 주니어 개발자를 채용합니다.",
                new Long[]{11L, 23L, 44L, 64L}
        );
    }

    @Test
    void postDetailsSerializationTest() throws IOException {
        assertThat(json.write(postDetails)).isStrictlyEqualToJson("postDetails.json");
        assertThat(json.write(postDetails)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(postDetails)).extractingJsonPathNumberValue("@.id").isEqualTo(77);
        assertThat(json.write(postDetails)).hasJsonPath("@.company");
        assertThat(json.write(postDetails)).extractingJsonPathValue("@.company")
                .extracting("id").isEqualTo(1);
        assertThat(json.write(postDetails)).extractingJsonPathValue("@.company")
                .extracting("name").isEqualTo("(주)원티드랩");
        assertThat(json.write(postDetails)).hasJsonPath("@.address");
        assertThat(json.write(postDetails)).extractingJsonPathValue("@.address")
                .extracting("street").isEqualTo("올림픽로 300, 롯데월드타워 35층");
        assertThat(json.write(postDetails)).extractingJsonPathValue("@.address")
                .extracting("city").isEqualTo("송파구");
        assertThat(json.write(postDetails)).extractingJsonPathValue("@.address")
                .extracting("state").isEqualTo("서울특별시");
        assertThat(json.write(postDetails)).hasJsonPathStringValue("@.positionName");
        assertThat(json.write(postDetails)).extractingJsonPathStringValue("@.positionName")
                .isEqualTo("백엔드 주니어 개발자");
        assertThat(json.write(postDetails)).hasJsonPathNumberValue("@.reward");
        assertThat(json.write(postDetails)).extractingJsonPathNumberValue("@.reward").isEqualTo(1500000);
        assertThat(json.write(postDetails)).hasJsonPathArrayValue("@.skills");
        assertThat(json.write(postDetails)).extractingJsonPathValue("@.skills[0].name").isEqualTo("Java");
        assertThat(json.write(postDetails)).hasJsonPathStringValue("@.jobDescription");
        assertThat(json.write(postDetails)).extractingJsonPathStringValue("@.jobDescription", "원티드랩에서 백엔드 주니어 개발자를 채용합니다.");
        assertThat(json.write(postDetails)).hasJsonPathArrayValue("@.otherPostsByCompany");
        assertThat(json.write(postDetails)).extractingJsonPathValue("@.otherPostsByCompany[0]").isEqualTo(11);
    }

    @Test
    void postDetailsDeserializationTest() throws IOException {
        String expected = """
                {
                  "id": 77,
                  "company": {
                    "id": 1,
                    "name": "(주)원티드랩"
                  },
                  "address": {
                    "street": "올림픽로 300, 롯데월드타워 35층",
                    "city": "송파구",
                    "state": "서울특별시"
                  },
                  "positionName": "백엔드 주니어 개발자",
                  "reward": 1500000,
                  "skills": [
                    {
                      "name": "Java"
                    }
                  ],
                  "jobDescription": "원티드랩에서 백엔드 주니어 개발자를 채용합니다.",
                  "otherPostsByCompany": [
                    11,
                    23,
                    44,
                    64
                  ]
                }
                """;

        assertThat(json.parseObject(expected).id()).isEqualTo(77L);
        assertThat(json.parseObject(expected).company().id()).isEqualTo(1L);
        assertThat(json.parseObject(expected).company().name()).isEqualTo("(주)원티드랩");
        assertThat(json.parseObject(expected).address().street()).isEqualTo("올림픽로 300, 롯데월드타워 35층");
        assertThat(json.parseObject(expected).address().city()).isEqualTo("송파구");
        assertThat(json.parseObject(expected).address().state()).isEqualTo("서울특별시");
        assertThat(json.parseObject(expected).positionName()).isEqualTo("백엔드 주니어 개발자");
        assertThat(json.parseObject(expected).reward()).isEqualTo(1500000L);
        assertThat(json.parseObject(expected).skills()[0].name()).isEqualTo("Java");
        assertThat(json.parseObject(expected).jobDescription()).isEqualTo("원티드랩에서 백엔드 주니어 개발자를 채용합니다.");
        assertThat(json.parseObject(expected).otherPostsByCompany().length).isEqualTo(4);
        assertThat(json.parseObject(expected).otherPostsByCompany()[0]).isEqualTo(11);
    }
}
