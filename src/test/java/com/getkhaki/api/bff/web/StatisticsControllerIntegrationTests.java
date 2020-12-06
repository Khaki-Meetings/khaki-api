package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseIntegrationTest;
import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.DepartmentsStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.IntervalDte;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.TimeBlockSummaryResponseDto;
import com.getkhaki.api.bff.web.models.TrailingStatisticsResponseDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.MockMvcConfigurer;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class StatisticsControllerIntegrationTests extends BaseIntegrationTest {
    @LocalServerPort
    private int port;

    private final WebApplicationContext webApplicationContext;
    private MockMvc mvc;

    public StatisticsControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @BeforeEach
    public void setupAuth() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        MockMvcConfigurer thing = springSecurity();
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void testOrganizationStatistics() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-08T00:00:00.000Z");

        String url = "/statistics/organizers/" +
                start.toString() +
                "/" +
                end.toString();

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT()).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result).isNotNull();
        OrganizersStatisticsResponseDto stats = (OrganizersStatisticsResponseDto) convertJSONStringToObject(
                result.getResponse().getContentAsString(),
                OrganizersStatisticsResponseDto.class
        );

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

        Jwt jwtToken = getJWT();

        String url = "/statistics/summary/" +
                start.toString() +
                "/" +
                end.toString();
        TimeBlockSummaryResponseDto stats = given()
//                .header(
//                        "Authorization",
//                        "Bearer " + jwtToken.toString()
//                )
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

    private static Jwt getJWT() {
        String tokenValue = "eyJraWQiOiJUVzhaWVZ0cTR1WlNMWkYyd2U2UVp5aG9GMDd0eUVpNE04cGJVa1pBOGpRIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULnc3MHBxTXpJNl94bVJxNWdYalRKbmRrTGQtTDFqQlM0UFpmbC1ZSXZ3LTgiLCJpc3MiOiJodHRwczovL2Rldi01MDU5NTAub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTk5NjY3MDk0LCJleHAiOjE1OTk2NzA2OTQsImNpZCI6IjBvYW00Z21uN3dUTTJqMTFXNHg2IiwidWlkIjoiMDB1dWhsOW50U1ZDSVppcnQ0eDYiLCJzY3AiOlsib3BlbmlkIiwicHJvZmlsZSIsImVtYWlsIl0sInN1YiI6Im1lQHByYXRobWVzaHBldGhrYXIuY29tIiwicm9sZXMiOlsiU3R1ZGVudCJdfQ.JvL3rWbc8DR66X6j_71YKIrqnu3rAGibi6wrBmkzwniICG-nixC2suFJ1KVHXZlY-YVA9Ylimr_iOi0guCn_9CyV9QzcJK2jqq5N4F-ragvvSODoKbPwGjTm7hnMCmt1xnrx-vz_vka4dzjmLgalqkcOB1r0wL4LAVjFJi9j5nWDe9ovB8eRsAcpseYoI96fkm5cExErgc2ayTOkyTLYGDIT3Je2QmDgzsepoKI5cpqrF1bCLJd3LcMcA0JLJctYQI9XpmYQuSBPyA3GdtH1ORX6_KBsjb58v7Lzy36etUlmMyTNmhXZqX1WwOi2SFgqPz_JJP7pcVmJnF9krbOfSw";

        Map<String, Object> headers = new HashMap<>();
        headers.put("testheader", "testheader");

        Map<String, Object> claims = new HashMap<>();
        List<String> aud = new ArrayList<>();
        aud.add("wgu");
        claims.put(JwtClaimNames.AUD, aud);
        claims.put(JwtClaimNames.SUB, "abc@test.com");
        List<String> roles = new ArrayList<>();
        roles.add("NGP_Learner");
        claims.put("roles", roles);
        claims.put(
                SessionTenant.CLAIMS_KEY,
                Map.of(
                        "s56_net",
                        "d713ace2-0d30-43be-b4ba-db973967d6d4"
                )
        );
        List<String> scp = new ArrayList<>();
        scp.add("write");
        claims.put("scp", scp);

        return new Jwt(tokenValue, null, null, headers, claims);
    }
}
