package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Betty')]").exists())
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Betty')].totalSeconds").value(9 * 3600))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Betty')].totalMeetings").value(1))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Bob')]").exists())
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Bob')].totalSeconds").value(18000))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Bob')].totalMeetings").value(2));
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
        assertThat(itDepartment.getTotalSeconds()).isEqualTo(8 * 3600);

        DepartmentStatisticsResponseDto hrDepartment = stats.getDepartmentsStatistics()
                .stream()
                .filter(stat -> stat.getDepartment().equals("HR"))
                .findFirst()
                .orElseThrow();
        assertThat(itDepartment.getTotalSeconds()).isEqualTo(8 * 3600);
    }

    @Test
    public void testTrailingStatistics() throws Exception {
        Instant start = Instant.parse("2020-11-02T00:00:00.000Z");
        int count = 2;

        String url = String.format("/statistics/trailing/%s/%s/%d", start, IntervalDte.Day, count);

        TrailingStatisticsResponseDto stats = getTypedResult(url, TrailingStatisticsResponseDto.class);

        assertThat(stats.getTimeBlockSummaries()).hasSize(2);

        List<TimeBlockSummaryResponseDto> summaries = stats.getTimeBlockSummaries();
        assertThat(summaries.get(0).getTotalSeconds()).isEqualTo(32400L);
        assertThat(summaries.get(0).getMeetingCount()).isEqualTo(1);

        assertThat(summaries.get(1).getTotalSeconds()).isEqualTo(18000L);
        assertThat(summaries.get(1).getMeetingCount()).isEqualTo(2);
    }

    @Test
    public void testTimeBlockSummary() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-18T00:00:00.000Z");

        String url = String.format("/statistics/summary/%s/%s", start, end);
        val stats = getTypedResult(url, TimeBlockSummaryResponseDto.class);

        assertThat(stats.getMeetingCount()).isEqualTo(4);
        assertThat(stats.getTotalSeconds()).isEqualTo(57600);

    }
}
