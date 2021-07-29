package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.web.models.*;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EmployeeControllerIntegrationTests extends BaseMvcIntegrationTest {

    public EmployeeControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Test
    public void getEmployees() throws Exception {
        MvcResult result = getMvcResult("/employees");

        mvc.perform(MockMvcRequestBuilders.get("/employees")
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')]").exists())
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].lastName").value("Jones"))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].department").value("HR"));
    }

    @Test
    public void getEmployeesForDepartment() throws Exception {
        MvcResult result = getMvcResult("/employees?department=HR");

        mvc.perform(MockMvcRequestBuilders.get("/employees?department=HR")
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')]").exists())
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].lastName").value("Jones"))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].department").value("HR"));
    }

    @Test
    public void getEmployeesWithStatistics() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-30T00:00:00.000Z");
        String url = String.format("/employees/statistics/%s/%s", start, end);

        var x = mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))));

        mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')]").exists())
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].lastName").value("Jones"))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].department").value("HR"))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].totalMeetings").value(4))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].totalSeconds").value(25200));
    }

    @Test
    public void getEmployeesWithStatisticsForDepartment() throws Exception {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-30T00:00:00.000Z");
        String url = String.format("/employees/statistics/%s/%s?department=HR", start, end);

        var x = mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))));

        mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')]").exists())
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].lastName").value("Jones"))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].department").value("HR"))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].totalMeetings").value(4))
                .andExpect(jsonPath("$.content[?(@.firstName == 'Bob')].totalSeconds").value(25200));
    }

    @Test
    @SneakyThrows
    public void success_getEmployee() {
        String url = "/employees/userProfile";
        MvcResult result = getMvcResult(url);

        UserProfileResponseDto userProfileResponseDto = (UserProfileResponseDto) convertJSONStringToObject(
                result.getResponse().getContentAsString(),
                UserProfileResponseDto.class
        );

        assertThat(userProfileResponseDto.getFirstName()).isEqualTo("Bob");
    }

    @Test
    @SneakyThrows
    public void success_getEmployeeWithoutPerson() {
        String url = "/employees/userProfile";
        MvcResult result = getMvcResult(url, "john@news.com");

        UserProfileResponseDto userProfileResponseDto = (UserProfileResponseDto) convertJSONStringToObject(
                result.getResponse().getContentAsString(),
                UserProfileResponseDto.class
        );

        assertThat(userProfileResponseDto.getFirstName()).isNullOrEmpty();
//        assertThat(userProfileResponseDto.getEmail()).isEqualTo("john@news.com");
    }

    @Test
    public void setUserProfile() throws Exception {

        String personId = "f66d66d7-7b40-4ffe-a38a-aae70919a1ef";
        String url = String.format("/employees/userProfile/%s", personId);

        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto();
        userProfileResponseDto.setFirstName("NewBobFirst");
        userProfileResponseDto.setLastName("NewBobLast");
        userProfileResponseDto.setDepartment("IT");

        String content = asJsonString(userProfileResponseDto);

        mvc.perform(MockMvcRequestBuilders.put(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(content)
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("NewBobFirst"))
                .andExpect(jsonPath("$.lastName").value("NewBobLast"))
                .andExpect(jsonPath("$.department").value("IT"));

    }
}
