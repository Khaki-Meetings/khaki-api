package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticsControllerIntegrationTests extends BaseJpaIntegrationTest {
    @LocalServerPort
    private int port;

    public StatisticsControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void testOrganizationStatistics() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-08T00:00:00.000Z");

        String urlStringBuilder = "/statistics/organizersStatistics/" +
                start.toString() +
                "/" +
                end.toString();
        OrganizersStatisticsResponseDto stats = given()
                .port(this.port)
                .contentType(JSON)
                .when()
                .get(urlStringBuilder)
                .then().assertThat()
                .statusCode(200)
                .extract()
                .as(OrganizersStatisticsResponseDto.class);

        OrganizerStatisticsResponseDto bettyStats = stats.getOrganizersStatistics()
                .stream()
                .filter(stat -> stat.getOrganizerEmail().equals("betty@s56.net"))
                .findFirst()
                .orElseThrow();
        assertThat(bettyStats.getTotalCost()).isEqualTo(95.0);
        assertThat(bettyStats.getTotalHours()).isEqualTo(2);
        assertThat(bettyStats.getTotalMeetingCount()).isEqualTo(1);

        OrganizerStatisticsResponseDto bobStats = stats.getOrganizersStatistics()
                .stream()
                .filter(stat -> stat.getOrganizerEmail().equals("bob@s56.net"))
                .findFirst()
                .orElseThrow();
        assertThat(bobStats.getOrganizerEmail()).isEqualTo("bob@s56.net");
        assertThat(bobStats.getTotalMeetingCount()).isEqualTo(1);
        assertThat(bobStats.getTotalHours()).isEqualTo(4);
        assertThat(bobStats.getTotalCost()).isEqualTo(190.0);
    }
}
