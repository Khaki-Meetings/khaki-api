package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalendarEventControllerIntegrationTests extends BaseMvcIntegrationTest {

    CalendarEventControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void createEvent() throws Exception {
        Instant now = Instant.now();
        CalendarEventDto body = new CalendarEventDto()
                .setGoogleCalendarId("12345")
                .setSummary("Summary")
                .setCreated(now)
                .setStart(now)
                .setEnd(now.plus(1, ChronoUnit.HOURS));

        String bodyString = asJsonString(body);
        String url = "/calendar-events";
        MvcResult result = mvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(bodyString)
                        .header(SessionTenant.HEADER_KEY, "s56_net")
                        .with(jwt().jwt(getJWT()).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andReturn();
    }

}
