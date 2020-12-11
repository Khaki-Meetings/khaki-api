package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.DepartmentsStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.IntervalDte;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.TimeBlockSummaryResponseDto;
import com.getkhaki.api.bff.web.models.TrailingStatisticsResponseDto;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class StatisticsControllerIntegrationTests extends BaseMvcIntegrationTest {
    public StatisticsControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        this.webApplicationContext = webApplicationContext;
    }

    @Test
    public void testOrganizationStatistics() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-08T00:00:00.000Z");

        String url = String.format("/statistics/organizers/%s/%s", start, end);

        OrganizersStatisticsResponseDto stats = getTypedResult(url, OrganizersStatisticsResponseDto.class);

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
    public void testDepartmentStatistics() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-08T00:00:00.000Z");

        String url = String.format("/statistics/department/%s/%s", start, end);
        DepartmentsStatisticsResponseDto stats = getTypedResult(url, DepartmentsStatisticsResponseDto.class);

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
    public void testTrailingStatistics() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        int count = 2;

        String url = String.format("/statistics/trailing/%s/%s/%d", start, IntervalDte.Day, count);

        TrailingStatisticsResponseDto stats = getTypedResult(url, TrailingStatisticsResponseDto.class);

        assertThat(stats.getTimeBlockSummaries()).hasSize(2);

        List<TimeBlockSummaryResponseDto> summaries = stats.getTimeBlockSummaries();
        assertThat(summaries.get(0).getTotalHours()).isEqualTo(4);
        assertThat(summaries.get(0).getMeetingCount()).isEqualTo(1);

        assertThat(summaries.get(1).getTotalHours()).isEqualTo(9);
        assertThat(summaries.get(1).getMeetingCount()).isEqualTo(1);
    }

    @Test
    public void testTimeBlockSummary() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-18T00:00:00.000Z");

        String url = String.format("/statistics/summary/%s/%s", start, end);
        val stats = getTypedResult(url, TimeBlockSummaryResponseDto.class);

        assertThat(stats.getMeetingCount()).isEqualTo(3);
        assertThat(stats.getTotalHours()).isEqualTo(15);

    }
}
