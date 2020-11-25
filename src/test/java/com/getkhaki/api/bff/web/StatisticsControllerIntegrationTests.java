package com.getkhaki.api.bff.web;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticsControllerIntegrationTests {
    @LocalServerPort
    private int port;

    public StatisticsControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void testOrganizationStatistics() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-30T00:00:00.000Z");

        StringBuilder urlStringBuilder = new StringBuilder();
        urlStringBuilder.append("/statistics/organizersStatistics/")
                .append(start.toString())
                .append("/")
                .append(end.toString());

        given()
                .port(this.port)
                .contentType(JSON)
                .when()
                .get(urlStringBuilder.toString())
                .then().assertThat()
                .statusCode(200)
                .body("page", notNullValue())
                .body("organizersStatistics", notNullValue())
        ;
    }
}
