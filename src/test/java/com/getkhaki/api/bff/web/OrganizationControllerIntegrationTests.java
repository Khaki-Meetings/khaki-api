package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseIntegrationTest;
import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.security.AuthenticationFacade;
import com.getkhaki.api.bff.web.models.OrganizationDto;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrganizationControllerIntegrationTests extends BaseIntegrationTest {
    @LocalServerPort
    private int port;

    OrganizationControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void getOrganization() throws Exception {
        String url = "/organizations";
        MvcResult result = getMvcResult(url);

        assertThat(result).isNotNull();
        OrganizationDto organization = (OrganizationDto) convertJSONStringToObject(
                result.getResponse().getContentAsString(),
                OrganizationDto.class
        );

        assertThat(organization.getName()).isEqualTo("S56");

    }
}
