package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.web.models.EmployeeDto;
import com.getkhaki.api.bff.web.models.UserProfileResponseDto;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EmployeeControllerIntegrationTests extends BaseMvcIntegrationTest {

    public EmployeeControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Test
    public void getEmployees() throws Exception {
        MvcResult result = getMvcResult("/employees");
        assertThat(result).isNotNull();

        Page<EmployeeDto> response = (Page<EmployeeDto>) convertJSONStringToObject(
                result.getResponse().getContentAsString(),
                PageImpl.class
        );

        assertThat(response).isNotNull();
        assertThat(response.getTotalElements()).isEqualTo(3);

        val bobJones = response
                .stream()
                .filter(employeeDto -> employeeDto.getEmail().equals("bob@s56.net"))
                .findFirst()
                .orElseThrow();

        assertThat(bobJones.getDepartment()).isEqualTo("HR");
        assertThat(bobJones.getFirstName()).isEqualTo("Bob");
        assertThat(bobJones.getLastName()).isEqualTo("Jones");
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
