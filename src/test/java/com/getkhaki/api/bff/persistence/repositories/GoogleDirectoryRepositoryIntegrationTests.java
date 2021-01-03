package com.getkhaki.api.bff.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

import static org.junit.platform.commons.util.Preconditions.notEmpty;

@SpringBootTest
public class GoogleDirectoryRepositoryIntegrationTests {
    @Inject
    private GoogleDirectoryRepository underTest;

    @Test
    public void getUsers() {
        // TODO: make a test that will work in pipeline
        //notEmpty(this.underTest.getUsers("casey@s56.net"), "getUsers should not return an empty list");
    }
}
