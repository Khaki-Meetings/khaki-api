package com.getkhaki.api.bff;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import java.io.IOException;
import java.util.UUID;

public abstract class BaseIntegrationTest {
    @Inject
    SpringLiquibase liquibase;

    protected UUID s56OrgUuid = UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4");

    @BeforeEach
    public void setup() throws LiquibaseException {
        liquibase.setChangeLog("classpath:fixtures/liquibase-test-data.yaml");
        liquibase.setShouldRun(true);
        liquibase.afterPropertiesSet();
    }

    public static <T> Object convertJSONStringToObject(String json, Class<T> objectClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        return mapper.readValue(json, objectClass);
    }
}
