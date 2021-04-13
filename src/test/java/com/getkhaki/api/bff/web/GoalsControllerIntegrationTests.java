package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseMvcIntegrationTest;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class GoalsControllerIntegrationTests extends BaseMvcIntegrationTest {

    public GoalsControllerIntegrationTests(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
        this.webApplicationContext = webApplicationContext;
    }

    @Test
    public void testGetGoals() throws Exception {
        String url = String.format("/goals");
        mvc.perform(MockMvcRequestBuilders.get(url)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT("bob@s56.net")).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.goals.length()").value(1))
                .andExpect(jsonPath("$.goals[?(@.measure == 'AttendeesPerMeeting')]").exists())
                .andExpect(jsonPath("$.goals[?(@.measure == 'AttendeesPerMeeting')].lessThanOrEqualTo").value(10));
    }
}
