package com.pedrcpereira.nkbeauty.common.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(ErrorTestController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvcTester mvc;

    @Test
    void malformedJson_returnsProblemDetail() {
        assertThat(mvc.post().uri("/test-errors/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":"))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
    }

    @Test
    void wrongDataType_returnsProblemDetail() {
        assertThat(mvc.get().uri("/test-errors/type?number=abc"))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
    }

    @Test
    void wrongRequestType_returnsProblemDetail() {
        assertThat(mvc.delete().uri("/test-errors/type"))
                .hasStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
    }

    @Test
    void invalidData_returnsProblemDetail() {
        assertThat(mvc.post().uri("/test-errors/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name":"","email":"not-an-email"}
                        """))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyJson().extractingPath("$.errors").asMap()
                .containsOnlyKeys("name", "email");
    }

    @Test
    void notFound_returnsProblemDetail() {
        assertThat(mvc.get().uri("/test-errors/not-found"))
                .hasStatus(HttpStatus.NOT_FOUND)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
    }

    @Test
    void conflict_returnsProblemDetailWithProperties() {
        assertThat(mvc.get().uri("/test-errors/conflict"))
                .hasStatus(HttpStatus.CONFLICT)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyJson().extractingPath("$.field").isEqualTo("email");
    }

    @Test
    void unexpected_returnsProblemDetailWithFixedMessage() {
        assertThat(mvc.get().uri("/test-errors/unexpected"))
                .hasStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .hasContentType(MediaType.APPLICATION_PROBLEM_JSON)
                .bodyText().doesNotContain("secret detail");
    }
}
