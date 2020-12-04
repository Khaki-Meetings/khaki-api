package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;

@ActiveProfiles(profiles = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartmentControllerIntegrationTests extends BaseJpaIntegrationTest {
    @LocalServerPort
    private int port;

    DepartmentControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void importAsync() {
        var csvContent = "firstName,lastName,email,department\r\nbob,dole,bob@dole.com,politicians";

        given()
                .port(this.port)
                .contentType("text/csv")
                .body(csvContent)
                .when()
                .post("/departments/import")
                .then().assertThat()
                .statusCode(200);
    }
}
