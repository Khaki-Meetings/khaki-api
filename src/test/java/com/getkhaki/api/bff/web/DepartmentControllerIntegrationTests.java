package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.web.models.DepartmentsResponseDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartmentControllerIntegrationTests extends BaseMvcIntegrationTest {
    DepartmentControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void importAsync() throws IOException {
    }

    @Test
    public void getDepartments() throws Exception {
        MvcResult result = getMvcResult("/departments");
        assertThat(result).isNotNull();

        DepartmentsResponseDto employeesResponseDto = (DepartmentsResponseDto) convertJSONStringToObject(
                result.getResponse().getContentAsString(),
                DepartmentsResponseDto.class
        );

        assertThat(employeesResponseDto).isNotNull();

        val foundDepartments = employeesResponseDto
                .getDepartments()
                .stream()
                .filter(departmentDto -> departmentDto.getName().matches("HR|IT"))
                .count();

        assertThat(foundDepartments).isEqualTo(2);
    }
}
