package com.getkhaki.api.bff;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;
import java.util.UUID;

public abstract class BaseJpaIntegrationTest {
    @Inject
    SpringLiquibase liquibase;

    protected UUID s56OrgUuid = UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4");

    @BeforeEach
    public void setup() throws LiquibaseException {
        liquibase.setChangeLog("classpath:fixtures/liquibase-test-data.yaml");
        liquibase.setShouldRun(true);
        liquibase.afterPropertiesSet();
    }
}
