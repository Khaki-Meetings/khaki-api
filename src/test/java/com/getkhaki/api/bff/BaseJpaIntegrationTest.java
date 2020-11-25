package com.getkhaki.api.bff;

import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.BeforeEach;

import javax.inject.Inject;

public abstract class BaseJpaIntegrationTest {
    @Inject
    SpringLiquibase liquibase;

    @BeforeEach
    public void setup() throws LiquibaseException {
        liquibase.setChangeLog("classpath:fixtures/liquibase-test-data.yaml");
        liquibase.setShouldRun(true);
        liquibase.afterPropertiesSet();
    }
}
