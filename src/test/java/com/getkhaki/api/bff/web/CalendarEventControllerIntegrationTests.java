package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import com.getkhaki.api.bff.web.models.CalendarEventsWithAttendeesResponseDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    /*
    @GetMapping("/{start}/{end}")
    public Page<CalendarEventsWithAttendeesResponseDto> getCalendarEventsAttendees(
            @PathVariable Instant start,
            @PathVariable Instant end,
            @RequestParam(required = false) String organizer,
            Pageable pageable) {
        Page<CalendarEventsWithAttendeesResponseDto> calendarEventsWithAttendeesDmList = calendarEventService
                .getCalendarEventsAttendees(start, end, organizer, pageable);
        return calendarEventsWithAttendeesDmList.map(dm -> modelMapper.map(dm, CalendarEventsWithAttendeesResponseDto.class));
    }*/

    @Test
    public void getCalendarEventsAttendees() throws Exception {
       // var employeeId = UUID.fromString("f66d66d7-7b40-4ffe-a38a-aae70919a1ef");
        String employeeId = "f66d66d7-7b40-4ffe-a38a-aae70919a1ef";
        var start = Instant.parse("2020-11-01T00:00:00.000Z");
        var end = Instant.parse("2020-11-08T00:00:00.000Z");
//        var url = String.format("/statistics/individual/%s/%s/%s", employeeId, start, end);
        var url = String.format("/calendar-events/%s/%s", start, end);
// /calendar-events/2020-12-01T00:00:00Z/2021-12-25T23:59:59Z?count=20&page=0&sort=totalSeconds,DESC&organizer=
        mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk());
        /*
                .andExpect(jsonPath("$.totalSeconds").exists())
                .andExpect(jsonPath("$.totalSeconds").value(21600))
                .andExpect(jsonPath("$.meetingCount").exists())
                .andExpect(jsonPath("$.meetingCount").value(3))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.start").value(start.toString()))
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.end").value(end.toString()));

         */
    }
}
