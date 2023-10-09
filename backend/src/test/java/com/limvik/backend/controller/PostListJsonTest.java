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
public class PostListJsonTest {

    @Autowired
    JacksonTester<PostView> json;

    private static PostView postList;

    @BeforeAll
    static void init() {
        postList = new PostView(
                77L,
                new CompanyView(1L, "(주)원티드랩"),
                new AddressView("올림픽로 300, 롯데월드타워 35층", "송파구", "서울특별시"),
                "백엔드 주니어 개발자",
                1500000L,
                new SkillView[]{new SkillView("Java")}
        );
    }

    @Test
    void postListSerializationTest() throws IOException {
        assertThat(json.write(postList)).isStrictlyEqualToJson("postList.json");
        assertThat(json.write(postList)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(postList)).extractingJsonPathNumberValue("@.id").isEqualTo(77);
        assertThat(json.write(postList)).hasJsonPath("@.company");
        assertThat(json.write(postList)).extractingJsonPathValue("@.company")
                .extracting("id").isEqualTo(1);
        assertThat(json.write(postList)).extractingJsonPathValue("@.company")
                .extracting("name").isEqualTo("(주)원티드랩");
        assertThat(json.write(postList)).hasJsonPath("@.address");
        assertThat(json.write(postList)).extractingJsonPathValue("@.address")
                .extracting("street").isEqualTo("올림픽로 300, 롯데월드타워 35층");
        assertThat(json.write(postList)).extractingJsonPathValue("@.address")
                .extracting("city").isEqualTo("송파구");
        assertThat(json.write(postList)).extractingJsonPathValue("@.address")
                .extracting("state").isEqualTo("서울특별시");
        assertThat(json.write(postList)).hasJsonPathStringValue("@.positionName");
        assertThat(json.write(postList)).extractingJsonPathStringValue("@.positionName")
                .isEqualTo("백엔드 주니어 개발자");
        assertThat(json.write(postList)).hasJsonPathNumberValue("@.reward");
        assertThat(json.write(postList)).extractingJsonPathNumberValue("@.reward").isEqualTo(1500000);
        assertThat(json.write(postList)).hasJsonPathArrayValue("@.skills");
        assertThat(json.write(postList)).extractingJsonPathValue("@.skills[0].name").isEqualTo("Java");
    }

    @Test
    void postListDeserializationTest() throws IOException {
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
    }
}
