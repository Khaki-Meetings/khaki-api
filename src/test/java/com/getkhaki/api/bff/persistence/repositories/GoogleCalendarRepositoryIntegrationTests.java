package com.getkhaki.api.bff.persistence.repositories;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest
public class GoogleCalendarRepositoryIntegrationTests {
    @Inject
    private GoogleCalendarRepository underTest;


    @Test
    public void getEvents() {
        val backValue = Instant.now().minus(1, ChronoUnit.DAYS);
        // TODO: make a test that will work in pipeline
//        this.underTest.getEvents("casey@s56.net", "casey@s56.net", backValue);
    }
}
