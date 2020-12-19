package com.getkhaki.api.bff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase
@Transactional
public abstract class BaseMvcIntegrationTest extends BaseJpaIntegrationTest {
    protected UUID s56OrgUuid = UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4");

    protected WebApplicationContext webApplicationContext;
    protected MockMvc mvc;

    protected BaseMvcIntegrationTest(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @BeforeEach
    public void setupAuth() {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    public static <T> Object convertJSONStringToObject(String json, Class<T> objectClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        return mapper.readValue(json, objectClass);
    }

    @AfterEach
    public void clearDb() {

    }

    public static String asJsonString(final Object obj) {
        try {
            val timeModule = new JavaTimeModule();
            final ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(timeModule);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static Jwt getJWT() {
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

    @SuppressWarnings("unchecked")
    protected <T> T getTypedResult(String urlString, Class<T> type) throws Exception {
        return  (T) convertJSONStringToObject(getMvcResult(urlString)
                .getResponse()
                .getContentAsString(), type);
    }

    protected MvcResult getMvcResult(String urlString) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get(urlString)
                .header(SessionTenant.HEADER_KEY, "s56_net")
                .with(jwt().jwt(getJWT()).authorities(new SimpleGrantedAuthority("admin"))))
                .andExpect(status().isOk())
                .andReturn();
    }
}
