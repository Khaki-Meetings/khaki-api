package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import com.getkhaki.api.bff.web.models.PersonDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@ActiveProfiles(profiles = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerIntegrationTests extends BaseJpaIntegrationTest {
    @LocalServerPort
    private int port;

    PersonControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void getPerson() {
        given()
                .port(this.port)
                .contentType(JSON)
                .when()
                .get("/persons/bob@s56.net")
                .then().assertThat()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    public void updatePerson() {
        PersonDto personDto = PersonDto.builder()
                .id(UUID.fromString("580cf117-aee1-433e-90ed-51c23a9b6e47"))
                .firstName("Fred")
                .build();

        given()
                .port(this.port)
                .contentType(JSON)
                .body(personDto)
                .when()
                .post("/persons")
                .then().assertThat()
                .statusCode(200)
                .body("firstName", is("Fred"));
    }
}
