package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.web.models.EmployeeDto;
import com.getkhaki.api.bff.web.models.UserProfileResponseDto;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

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
}
