package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.BaseModelMapperIntegrationTests;
import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DmToDtoMapperIntegrationTests extends BaseModelMapperIntegrationTests {
    @Test
    public void calendarEvent() {
        CalendarEventDm calendarEventDm = new CalendarEventDm()
                .setId(UUID.randomUUID())
                .setSummary("cignus")
                .setCreated(LocalDateTime.now());

        CalendarEventDto calendarEventDto = modelMapper.map(calendarEventDm, CalendarEventDto.class);

        assertThat(calendarEventDto).isNotNull();
        assertThat(calendarEventDto.getId()).isEqualTo(calendarEventDm.getId());
        assertThat(calendarEventDto.getSummary()).isEqualTo(calendarEventDm.getSummary());
        assertThat(calendarEventDto.getCreated()).isEqualTo(calendarEventDm.getCreated());
    }
}
