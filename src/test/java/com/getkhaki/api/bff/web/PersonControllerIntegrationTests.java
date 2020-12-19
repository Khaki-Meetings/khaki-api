package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PersonControllerIntegrationTests extends BaseMvcIntegrationTest {
    PersonControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void getPerson() throws Exception {
        PersonDto personDto = getTypedResult("/persons/bob@s56.net", PersonDto.class);
        assertThat(personDto.getFirstName()).isEqualTo("Bob");
    }

    @Test
    public void updatePerson() throws Exception {
        PersonDto personDto = PersonDto.builder()
                .id(UUID.fromString("580cf117-aee1-433e-90ed-51c23a9b6e47"))
                .firstName("Fred")
                .build();

        String bodyString = asJsonString(personDto);

        mvc.perform(
                post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(bodyString)
                        .header(SessionTenant.HEADER_KEY, "s56_net")
                        .with(jwt().jwt(getJWT()).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andReturn();
    }
}
