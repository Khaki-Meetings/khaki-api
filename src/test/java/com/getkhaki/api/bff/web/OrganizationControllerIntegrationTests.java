package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseIntegrationTest;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ActiveProfiles(profiles = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrganizationControllerIntegrationTests extends BaseIntegrationTest {
    @LocalServerPort
    private int port;

    OrganizationControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void getOrganization() {
        given()
                .port(this.port)
                .contentType(JSON)
                .when()
                .get("/organizations")
                .then().assertThat()
                .statusCode(200)
                .body(notNullValue());
    }
}
