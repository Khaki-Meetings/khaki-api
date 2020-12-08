package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.web.models.DepartmentsResponseDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(profiles = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartmentControllerIntegrationTests extends BaseMvcIntegrationTest {
    DepartmentControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void importAsync() throws IOException {
//        given()
//                .port(this.port)
//                .multiPart(new ClassPathResource("department-import.csv").getFile())
//                .when()
//                .post("/departments/import")
//                .then().assertThat()
//                .statusCode(200);
    }

    @Test
    public void getDepartments() throws Exception {
        String url = "/departments";
        MvcResult result = getMvcResult(url);

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
