package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseIntegrationTest;
import com.getkhaki.api.bff.web.models.EmployeesResponseDto;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTests extends BaseIntegrationTest {

    public EmployeeControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Test
    public void getEmployees() throws Exception {
        String url = "/employees";
        MvcResult result = getMvcResult(url);

        assertThat(result).isNotNull();
        EmployeesResponseDto employeesResponseDto = (EmployeesResponseDto) convertJSONStringToObject(
                result.getResponse().getContentAsString(),
                EmployeesResponseDto.class
        );

        assertThat(employeesResponseDto).isNotNull();
        assertThat(employeesResponseDto.getEmployees()).hasSize(3);

        val bobJones = employeesResponseDto
                .getEmployees()
                .stream()
                .filter(
                        employeeDto -> employeeDto.getEmail().equals("bob@s56.net")
                )
                .findFirst()
                .orElseThrow();
        assertThat(bobJones.getDepartment()).isEqualTo("HR");
        assertThat(bobJones.getFirstName()).isEqualTo("Bob");
        assertThat(bobJones.getLastName()).isEqualTo("Jones");
    }
}
