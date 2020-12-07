package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.web.models.CalendarEventDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalendarEventControllerIntegrationTests {
    @LocalServerPort
    private int port;

    CalendarEventControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void createEvent() {
        Instant now = Instant.now();
        CalendarEventDto body = new CalendarEventDto()
                .setGoogleCalendarId("12345")
                .setSummary("Summary")
                .setCreated(now)
                .setStart(now)
                .setEnd(now.plus(1, ChronoUnit.HOURS));

        given()
                .port(this.port)
                .contentType(JSON)
                .body(body)
                .when()
                .post("/calendar-events")
                .then().assertThat()
                .statusCode(200)
                .body("id", notNullValue());
    }

    //ToDo: Take a look on it again
    @Test
    public void getEvents() {

        given()
                .port(this.port)
                .contentType(JSON)
                .body()
                .when()
                .get("/calendar-events")
                .then().assertThat()
                .statusCode(200)
                .body();

    }
}
