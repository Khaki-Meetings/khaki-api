package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DtoToDmMapperIntegrationTests extends BaseModelMapperIntegrationTests {
    @Test
    public void calendarEvent() {
        CalendarEventDto calendarEventDto = new CalendarEventDto()
                .setId(UUID.randomUUID())
                .setSummary("cignus")
                .setCreated(Instant.now());

        CalendarEventDm calendarEventDm = underTest.map(calendarEventDto, CalendarEventDm.class);

        assertThat(calendarEventDm).isNotNull();
        assertThat(calendarEventDm.getId()).isEqualTo(calendarEventDto.getId());
        assertThat(calendarEventDm.getSummary()).isEqualTo(calendarEventDto.getSummary());
        assertThat(calendarEventDm.getCreated()).isEqualTo(calendarEventDto.getCreated());
    }
}
