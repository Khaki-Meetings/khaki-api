package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoToDmCalendarEventIntegrationTests extends BaseModelMapperIntegrationTests {
    @Test
    public void calendarEvent() {
        CalendarEventDao calendarEventDao = new CalendarEventDao()
                .setSummary("cignus")
                .setCreated(Instant.now());
        calendarEventDao.setId(UUID.randomUUID());

        CalendarEventDm calendarEventDm = underTest.map(calendarEventDao, CalendarEventDm.class);

        assertThat(calendarEventDm).isNotNull();
        assertThat(calendarEventDm.getId()).isEqualTo(calendarEventDao.getId());
        assertThat(calendarEventDm.getSummary()).isEqualTo(calendarEventDao.getSummary());
        assertThat(calendarEventDm.getCreated()).isEqualTo(calendarEventDao.getCreated());
    }
}
