package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.DepartmentsStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.IntervalDte;
import com.getkhaki.api.bff.web.models.StatisticsFilterDte;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void testOrganizersStatisticsDefault() throws Exception {
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
    public void testOrganizersStatisticsInternal() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-30T00:00:00.000Z");
        String url = String.format("/statistics/organizers/%s/%s?filter=" + StatisticsFilterDte.Internal, start, end);

        mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Betty')]").exists())
                .andExpect(jsonPath("$.content[?(@.organizerLastName == 'Smith')]").exists())
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Betty')].totalSeconds").value(32400))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Betty')].totalMeetings").value(1))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Bob')]").exists())
                .andExpect(jsonPath("$.content[?(@.organizerLastName == 'Jones')]").exists())
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Bob')].totalSeconds").value(7200))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Bob')].totalMeetings").value(1));
    }

    @Test
    public void testOrganizersStatisticsAll() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-30T00:00:00.000Z");
        String url = String.format("/statistics/organizers/aggregate/%s/%s", start, end);

        mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Betty')]").exists())
                .andExpect(jsonPath("$.content[?(@.organizerLastName == 'Smith')]").exists())
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Betty')].internalMeetingSeconds").value(32400))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Betty')].internalMeetingCount").value(1))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Bob')]").exists())
                .andExpect(jsonPath("$.content[?(@.organizerLastName == 'Jones')]").exists())
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Bob')].externalMeetingSeconds").value(25200))
                .andExpect(jsonPath("$.content[?(@.organizerFirstName == 'Bob')].externalMeetingCount").value(3));
    }

    @Test
    public void testDepartmentStatisticsDefault() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-30T00:00:00.000Z");
        String url = String.format("/statistics/department/%s/%s", start, end);

        DepartmentsStatisticsResponseDto stats = getTypedResult(url, DepartmentsStatisticsResponseDto.class);

        DepartmentStatisticsResponseDto itDepartment = stats.getDepartmentsStatistics()
                .stream()
                .filter(stat -> stat.getDepartment().equals("IT"))
                .findFirst()
                .orElseThrow();

        assertThat(itDepartment.getTotalSeconds()).isEqualTo(32400);
        assertThat(itDepartment.getInventorySecondsAvailable()).isEqualTo(1152000);

        DepartmentStatisticsResponseDto hrDepartment = stats.getDepartmentsStatistics()
                .stream()
                .filter(stat -> stat.getDepartment().equals("HR"))
                .findFirst()
                .orElseThrow();

        assertThat(hrDepartment.getTotalSeconds()).isEqualTo(25200);
        assertThat(hrDepartment.getInventorySecondsAvailable()).isEqualTo(576000);

    }

    @Test
    public void testDepartmentStatisticsInternal() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-30T00:00:00.000Z");
        String url = String.format("/statistics/department/%s/%s?filter=" + StatisticsFilterDte.Internal, start, end);

        DepartmentsStatisticsResponseDto stats = getTypedResult(url, DepartmentsStatisticsResponseDto.class);

        DepartmentStatisticsResponseDto itDepartment = stats.getDepartmentsStatistics()
                .stream()
                .filter(stat -> stat.getDepartment().equals("IT"))
                .findFirst()
                .orElseThrow();

        assertThat(itDepartment.getTotalSeconds()).isEqualTo(25200);
        assertThat(itDepartment.getInventorySecondsAvailable()).isEqualTo(1152000);

        DepartmentStatisticsResponseDto hrDepartment = stats.getDepartmentsStatistics()
                .stream()
                .filter(stat -> stat.getDepartment().equals("HR"))
                .findFirst()
                .orElseThrow();

        assertThat(hrDepartment.getTotalSeconds()).isEqualTo(14400);
        assertThat(hrDepartment.getInventorySecondsAvailable()).isEqualTo(576000);

    }

    @Test
    public void testTrailingStatisticsDefault() throws Exception {
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
    public void testTrailingStatisticsInternal() throws Exception {
        Instant start = Instant.parse("2020-11-02T00:00:00.000Z");
        int count = 2;
        String url = String.format("/statistics/trailing/%s/%s/%d?filter=%s", start, IntervalDte.Day, count,
                StatisticsFilterDte.Internal);

        TrailingStatisticsResponseDto stats = getTypedResult(url, TrailingStatisticsResponseDto.class);

        assertThat(stats.getTimeBlockSummaries()).hasSize(2);

        List<TimeBlockSummaryResponseDto> summaries = stats.getTimeBlockSummaries();

        assertThat(summaries.get(0).getTotalSeconds()).isEqualTo(32400L);
        assertThat(summaries.get(0).getMeetingCount()).isEqualTo(1);
        assertThat(summaries.get(1).getTotalSeconds()).isNull();
        assertThat(summaries.get(1).getMeetingCount()).isEqualTo(0);
    }

    @Test
    public void testTimeBlockSummaryDefault() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-18T00:00:00.000Z");
        String url = String.format("/statistics/summary/%s/%s", start, end);

        val stats = getTypedResult(url, TimeBlockSummaryResponseDto.class);

        assertThat(stats.getMeetingCount()).isEqualTo(4);
        assertThat(stats.getTotalSeconds()).isEqualTo(57600);
    }

    @Test
    public void testTimeBlockSummaryInternal() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-18T00:00:00.000Z");
        String url = String.format("/statistics/summary/%s/%s?filter=%s", start, end, StatisticsFilterDte.Internal);

        val stats = getTypedResult(url, TimeBlockSummaryResponseDto.class);

        assertThat(stats.getMeetingCount()).isEqualTo(2);
        assertThat(stats.getTotalSeconds()).isEqualTo(39600);
    }

    @Test
    public void getIndividualStatistics() throws Exception {
        var employeeId = UUID.fromString("f66d66d7-7b40-4ffe-a38a-aae70919a1ef");
        var start = Instant.parse("2020-11-01T00:00:00.000Z");
        var end = Instant.parse("2020-11-08T00:00:00.000Z");
        var url = String.format("/statistics/individual/%s/%s/%s", employeeId, start, end);

        mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSeconds").exists())
                .andExpect(jsonPath("$.totalSeconds").value(21600))
                .andExpect(jsonPath("$.meetingCount").exists())
                .andExpect(jsonPath("$.meetingCount").value(3))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.start").value(start.toString()))
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.end").value(end.toString()));
    }
}
