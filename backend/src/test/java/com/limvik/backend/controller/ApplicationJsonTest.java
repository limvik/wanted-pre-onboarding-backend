package com.limvik.backend.controller;

import com.limvik.backend.dto.ApplicationView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ApplicationJsonTest {

    @Autowired
    JacksonTester<ApplicationView> json;

    static ApplicationView request;
    static ApplicationView response;
    static Instant instant;

    @BeforeAll
    static void init() {

        request = ApplicationView.applicationRequest(12L, 42L);

        instant = Instant.parse("2023-10-12T00:41:35.575Z");
        response = new ApplicationView(12L, 42L,
                instant,
                instant,
                "서류접수");

    }

    @Test
    void applicationRequestDeserializationTest() throws IOException {
        var expected = """
                {
                  "postId": 12,
                  "userId": 42
                }
                """;

        assertThat(json.parseObject(expected).postId()).isEqualTo(12);
        assertThat(json.parseObject(expected).userId()).isEqualTo(42);
    }

    @Test
    void applicationResponseSerializationTest() throws IOException {
        assertThat(json.write(response)).isStrictlyEqualToJson("applicationResponse.json");
        assertThat(json.write(response)).extractingJsonPathNumberValue("@.postId").isEqualTo(12);
        assertThat(json.write(response)).extractingJsonPathNumberValue("@.userId").isEqualTo(42);
        assertThat(json.write(response)).extractingJsonPathStringValue("@.appliedAt").isEqualTo(instant.toString());
        assertThat(json.write(response)).extractingJsonPathStringValue("@.updatedAt").isEqualTo(instant.toString());
        assertThat(json.write(response)).extractingJsonPathStringValue("@.status").isEqualTo("서류접수");
    }
}
