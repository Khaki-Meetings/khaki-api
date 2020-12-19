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
        val backValue = Instant.now().plus(-1, ChronoUnit.DAYS);
        this.underTest.getEvents("casey@s56.net", "casey@s56.net", backValue);
    }
}
