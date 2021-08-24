package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                Objects.requireNonNull(classLoader.getResource("s56-department-import.csv")).getFile()
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
        mvc.perform(MockMvcRequestBuilders.get("/departments")
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value(
                        either(is("HR")).or(is("IT")))
                );
    }

    @Test
    public void createDepartment() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/departments")
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value(
                        either(is("HR")).or(is("IT")))
                );

        String name = "newDept";
        String content = asJsonString(name);

        mvc.perform(MockMvcRequestBuilders.post("/departments")
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(content)
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("\"newDept\""));

        mvc.perform(MockMvcRequestBuilders.get("/departments")
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].name").value(
                        either(is("HR")).or(is("IT")))
                );
    }

}
