package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.web.models.OrganizationDto;
import com.getkhaki.api.bff.web.models.PersonDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrganizationControllerIntegrationTests extends BaseMvcIntegrationTest {
    OrganizationControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void getOrganization() throws Exception {
        OrganizationDto organization = getTypedResult("/organizations", OrganizationDto.class);

        assertThat(organization).isNotNull();
        assertThat(organization.getName()).isEqualTo("S56");
    }

    @Test
    public void createOrganization() throws Exception {
        OrganizationDto organizationDto = OrganizationDto.builder()
                .name("acme")
                .adminEmail("contact@acme.com")
                .build();

        mvc.perform(
                post("/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(organizationDto))
                        .header(SessionTenant.HEADER_KEY, "s56_net")
                        .with(jwt().jwt(getJWT()).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name").value(organizationDto.getName()))
                .andExpect(jsonPath("$.adminEmail").exists())
                .andExpect(jsonPath("$.adminEmail").value(organizationDto.getAdminEmail()))
                .andReturn();
    }
}
