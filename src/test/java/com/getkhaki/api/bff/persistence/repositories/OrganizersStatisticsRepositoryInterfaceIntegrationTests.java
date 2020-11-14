package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@DataJpaTest
public class OrganizersStatisticsRepositoryInterfaceIntegrationTests {
    @Inject
    private OrganizersStatisticsRepositoryInterface underTest;

    @Inject
    private CalendarEventRepositoryInterface calendarEventRepository;

    @Test
    public void queryTest() {
        CalendarEventDao calendarEvent = new CalendarEventDao()
                .setGoogleCalendarId("cal-id")
                .setCreated(ZonedDateTime.now())
                .setSummary("calEvent");

    }
}
