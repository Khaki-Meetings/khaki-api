package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.repositories.CalendarEventRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CalendarEventRepositoryIntegrationTest {
    @Inject
    private CalendarEventRepositoryInterface repository;

    @Test
    void testPersistence() {
        Instant now = Instant.now();
        CalendarEventDao dao = new CalendarEventDao()
                .setGoogleCalendarId("12345")
                .setSummary("Summary")
                .setCreated(now)
                .setStart(now)
                .setEnd(now.plus(1, ChronoUnit.HOURS));

        CalendarEventDao result = this.repository.save(dao);

        assertNotNull(result);
        assertEquals(dao.getGoogleCalendarId(), result.getGoogleCalendarId());
        assertEquals(dao.getSummary(), result.getSummary());
        assertEquals(dao.getCreated(), result.getCreated());
    }
}
