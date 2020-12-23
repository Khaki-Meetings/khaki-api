package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.web.models.DepartmentDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DepartmentControllerIntegrationTests extends BaseMvcIntegrationTest {
    DepartmentControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    @Test
    public void importAsync() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        Path filePath = Paths.get(
                Objects.requireNonNull(classLoader.getResource("department-import.csv")).getFile()
        );

        MockMultipartFile csvMultipart = new MockMultipartFile(
                "file",
                "department-import.csv",
                "text/plain",
                Files.readAllBytes(filePath)
        );
        mvc.perform(
                multipart("/departments/import")
                        .file(csvMultipart)
                        .header(SessionTenant.HEADER_KEY, "s56_net")
                        .with(jwt().jwt(getJWT()).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk());

        // Check idempotent
        mvc.perform(
                multipart("/departments/import")
                        .file(csvMultipart)
                        .header(SessionTenant.HEADER_KEY, "s56_net")
                        .with(jwt().jwt(getJWT()).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk());
    }

    @Test
    public void getDepartments() throws Exception {
        MvcResult result = getMvcResult("/departments");
        assertThat(result).isNotNull();

        Page<DepartmentDto> response = (Page<DepartmentDto>) convertJSONStringToObject(
                result.getResponse().getContentAsString(),
                PageImpl.class
        );

        assertThat(response).isNotNull();

        val foundDepartments = response
                .stream()
                .filter(departmentDto -> departmentDto.getName().matches("HR|IT"))
                .count();

        assertThat(foundDepartments).isEqualTo(2);
    }
}
