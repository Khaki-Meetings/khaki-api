package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.web.models.OrganizationDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrganizationControllerIntegrationTests extends BaseMvcIntegrationTest {
    OrganizationControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void getOrganization() throws Exception {
        String url = "/organizations";
        OrganizationDto organization = getTypedResult(url, OrganizationDto.class);

        assertThat(organization).isNotNull();

        assertThat(organization.getName()).isEqualTo("S56");

    }
}
