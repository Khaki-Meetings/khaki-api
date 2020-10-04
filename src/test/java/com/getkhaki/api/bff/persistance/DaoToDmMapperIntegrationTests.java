package com.getkhaki.api.bff.persistance;

import com.getkhaki.api.bff.BaseModelMapperIntegrationTests;
import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistance.models.CalendarEventDao;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoToDmMapperIntegrationTests extends BaseModelMapperIntegrationTests {
    @Test
    public void calendarEvent() {
        CalendarEventDao calendarEventDao = new CalendarEventDao()
                .setId(UUID.randomUUID())
                .setSummary("cignus")
                .setCreated(LocalDateTime.now());

        CalendarEventDm calendarEventDm = modelMapper.map(calendarEventDao, CalendarEventDm.class);

        assertThat(calendarEventDm).isNotNull();
        assertThat(calendarEventDm.getId()).isEqualTo(calendarEventDao.getId());
        assertThat(calendarEventDm.getSummary()).isEqualTo(calendarEventDao.getSummary());
        assertThat(calendarEventDm.getCreated()).isEqualTo(calendarEventDao.getCreated());
    }
}
