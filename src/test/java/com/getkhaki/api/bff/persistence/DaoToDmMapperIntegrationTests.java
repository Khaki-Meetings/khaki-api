package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.BaseModelMapperIntegrationTests;
import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoToDmMapperIntegrationTests extends BaseModelMapperIntegrationTests {
    @Test
    public void calendarEvent() {
        CalendarEventDao calendarEventDao = new CalendarEventDao()
                .setSummary("cignus")
                .setCreated(Instant.now());
        calendarEventDao.setId(UUID.randomUUID());

        CalendarEventDm calendarEventDm = modelMapper.map(calendarEventDao, CalendarEventDm.class);

        assertThat(calendarEventDm).isNotNull();
        assertThat(calendarEventDm.getId()).isEqualTo(calendarEventDao.getId());
        assertThat(calendarEventDm.getSummary()).isEqualTo(calendarEventDao.getSummary());
        assertThat(calendarEventDm.getCreated()).isEqualTo(calendarEventDao.getCreated());
    }
}
