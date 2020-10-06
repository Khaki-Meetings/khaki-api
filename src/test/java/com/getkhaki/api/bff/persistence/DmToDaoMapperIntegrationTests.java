package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.BaseModelMapperIntegrationTests;
import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DmToDaoMapperIntegrationTests extends BaseModelMapperIntegrationTests {
    @Test
    public void calendarEvent() {
        CalendarEventDm calendarEventDm = new CalendarEventDm()
                .setId(UUID.randomUUID())
                .setSummary("cignus")
                .setCreated(LocalDateTime.now());

        CalendarEventDao calendarEventDao = modelMapper.map(calendarEventDm, CalendarEventDao.class);

        assertThat(calendarEventDao).isNotNull();
        assertThat(calendarEventDao.getId()).isEqualTo(calendarEventDm.getId());
        assertThat(calendarEventDao.getSummary()).isEqualTo(calendarEventDm.getSummary());
        assertThat(calendarEventDao.getCreated()).isEqualTo(calendarEventDm.getCreated());
    }
}
