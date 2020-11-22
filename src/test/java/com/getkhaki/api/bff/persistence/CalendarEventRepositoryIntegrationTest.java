package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.repositories.CalendarEventRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CalendarEventRepositoryIntegrationTest {
    @Inject
    private CalendarEventRepositoryInterface repository;

    @Test
    void testPersistence() {
        ZonedDateTime now = ZonedDateTime.now();
        CalendarEventDao dao = new CalendarEventDao()
                .setGoogleCalendarId("12345")
                .setSummary("Summary")
                .setCreated(now)
                .setStart(now)
                .setEnd(now.plusHours(1)) ;

        CalendarEventDao result = this.repository.save(dao);

        assertNotNull(result);
        assertEquals(dao.getGoogleCalendarId(), result.getGoogleCalendarId());
        assertEquals(dao.getSummary(), result.getSummary());
        assertEquals(dao.getCreated(), result.getCreated());
    }
}
