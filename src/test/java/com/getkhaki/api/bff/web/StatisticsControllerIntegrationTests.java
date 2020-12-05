package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.DepartmentsStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.IntervalDte;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.TimeBlockSummaryResponseDto;
import com.getkhaki.api.bff.web.models.TrailingStatisticsResponseDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;

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

        String url = "/statistics/organizersStatistics/" +
                start.toString() +
                "/" +
                end.toString();
        OrganizersStatisticsResponseDto stats = given()
                .port(this.port)
                .contentType(JSON)
                .when()
                .get(url)
                .then().assertThat()
                .statusCode(200)
                .extract()
                .as(OrganizersStatisticsResponseDto.class);

        OrganizerStatisticsResponseDto bettyStats = stats.getOrganizersStatistics()
                .stream()
                .filter(stat -> stat.getOrganizerEmail().equals("betty@s56.net"))
                .findFirst()
                .orElseThrow();
        assertThat(bettyStats.getTotalCost()).isEqualTo(1282.5);
        assertThat(bettyStats.getTotalHours()).isEqualTo(9);
        assertThat(bettyStats.getTotalMeetings()).isEqualTo(1);

        OrganizerStatisticsResponseDto bobStats = stats.getOrganizersStatistics()
                .stream()
                .filter(stat -> stat.getOrganizerEmail().equals("bob@s56.net"))
                .findFirst()
                .orElseThrow();
        assertThat(bobStats.getOrganizerEmail()).isEqualTo("bob@s56.net");
        assertThat(bobStats.getTotalMeetings()).isEqualTo(1);
        assertThat(bobStats.getTotalHours()).isEqualTo(4);
        assertThat(bobStats.getTotalCost()).isEqualTo(380.0);
    }

    @Test
    public void testDepartmentStatistics() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-08T00:00:00.000Z");

        String url = "/statistics/department/" +
                start.toString() +
                "/" +
                end.toString();
        DepartmentsStatisticsResponseDto stats = given()
                .port(this.port)
                .contentType(JSON)
                .when()
                .get(url)
                .then().assertThat()
                .statusCode(200)
                .extract()
                .as(DepartmentsStatisticsResponseDto.class);

        DepartmentStatisticsResponseDto itDepartment = stats.getDepartmentsStatistics()
                .stream()
                .filter(stat -> stat.getDepartment().equals("IT"))
                .findFirst()
                .orElseThrow();
        assertThat(itDepartment.getTotalHours()).isEqualTo(8);

        DepartmentStatisticsResponseDto hrDepartment = stats.getDepartmentsStatistics()
                .stream()
                .filter(stat -> stat.getDepartment().equals("HR"))
                .findFirst()
                .orElseThrow();
        assertThat(itDepartment.getTotalHours()).isEqualTo(8);
    }

    @Test
    public void testTrailingStatistics() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        int count = 2;

        String url = String.format(
                "/statistics/trailing/%s/%s/%d",
                start.toString(),
                IntervalDte.Day,
                count
        );

        TrailingStatisticsResponseDto stats = given()
                .port(this.port)
                .contentType(JSON)
                .when()
                .get(url)
                .then().assertThat()
                .statusCode(200)
                .extract()
                .as(TrailingStatisticsResponseDto.class);

        assertThat(stats.getTimeBlockSummaries()).hasSize(2);

        List<TimeBlockSummaryResponseDto> summaries = stats.getTimeBlockSummaries();
        assertThat(summaries.get(0).getTotalHours()).isEqualTo(4);
        assertThat(summaries.get(0).getMeetingCount()).isEqualTo(1);

        assertThat(summaries.get(1).getTotalHours()).isEqualTo(9);
        assertThat(summaries.get(1).getMeetingCount()).isEqualTo(1);
    }

    @Test
    public void testTimeBlockSummary() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-18T00:00:00.000Z");

        String url = "/statistics/summary/" +
                start.toString() +
                "/" +
                end.toString();
        TimeBlockSummaryResponseDto stats = given()
                .port(this.port)
                .contentType(JSON)
                .when()
                .get(url)
                .then().assertThat()
                .statusCode(200)
                .extract()
                .as(TimeBlockSummaryResponseDto.class);

        assertThat(stats.getMeetingCount()).isEqualTo(3);
        assertThat(stats.getTotalHours()).isEqualTo(15);

    }

}
