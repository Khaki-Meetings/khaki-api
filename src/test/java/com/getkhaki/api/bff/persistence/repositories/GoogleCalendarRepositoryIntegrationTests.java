package com.getkhaki.api.bff.persistence.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

import static org.junit.platform.commons.util.Preconditions.notEmpty;

@SpringBootTest
public class GoogleCalendarRepositoryIntegrationTests {
    @Inject
    private GoogleCalendarRepository underTest;

    @Test
    public void getEvents() {
        notEmpty(this.underTest.getEvents("casey@s56.net", "casey@s56.net"),
                "getEvents should not return an empty list");
    }
}
