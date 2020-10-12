package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.repositories.CalendarEventRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CalendarEventRepositoryIntegrationTest {
    @Inject
    private CalendarEventRepositoryInterface repository;

    @Test
    void testPersistence() {
        CalendarEventDao dao = new CalendarEventDao()
                .setGoogleCalendarId("12345")
                .setSummary("Summary")
                .setCreated(LocalDateTime.now());

        CalendarEventDao result = this.repository.save(dao);

        assertNotNull(result);
        assertEquals(dao.getGoogleCalendarId(), result.getGoogleCalendarId());
        assertEquals(dao.getSummary(), result.getSummary());
        assertEquals(dao.getCreated(), result.getCreated());
    }
}
