package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

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
    public void importAsync() throws IOException {
        given()
                .port(this.port)
                .multiPart(new ClassPathResource("department-import.csv").getFile())
                .when()
                .post("/departments/import")
                .then().assertThat()
                .statusCode(200);
    }
}
