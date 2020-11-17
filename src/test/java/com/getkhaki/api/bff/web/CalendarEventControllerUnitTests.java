package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.services.CalendarEventService;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalendarEventControllerUnitTests {
    private CalendarEventController underTest;

    private CalendarEventService calendarEventService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        calendarEventService = mock(CalendarEventService.class);
        modelMapper = mock(ModelMapper.class);
        underTest = new CalendarEventController(calendarEventService, modelMapper);
    }

    @Test
    public void goodPath() {
        ZonedDateTime now = ZonedDateTime.now();
        CalendarEventDto calendarEventDtoInput = new CalendarEventDto();
        calendarEventDtoInput.setSummary("now");
        calendarEventDtoInput.setCreated(now);

        CalendarEventDm calendarEventDmMapped = new CalendarEventDm();
        calendarEventDmMapped.setCreated(calendarEventDtoInput.getCreated());
        calendarEventDmMapped.setSummary(calendarEventDtoInput.getSummary());

        when(modelMapper.map(calendarEventDtoInput, CalendarEventDm.class)).thenReturn(calendarEventDmMapped);

        UUID id = UUID.randomUUID();
        CalendarEventDm calendarEventDmMockedResponse = new CalendarEventDm()
                .setSummary(calendarEventDmMapped.getSummary())
                .setGoogleCalendarId(calendarEventDmMapped.getGoogleCalendarId())
                .setCreated(calendarEventDmMapped.getCreated())
                .setId(id);
        when(calendarEventService.createEvent(calendarEventDmMapped)).thenReturn(calendarEventDmMockedResponse);

        CalendarEventDto calendarEventDtoMockedResponse = new CalendarEventDto()
                .setSummary(calendarEventDmMockedResponse.getSummary())
                .setGoogleCalendarId(calendarEventDmMockedResponse.getGoogleCalendarId())
                .setCreated(calendarEventDmMockedResponse.getCreated())
                .setId(id);

        when(modelMapper.map(calendarEventDmMockedResponse, CalendarEventDto.class)).thenReturn(calendarEventDtoMockedResponse);

        CalendarEventDto calendarEventDtoResponse = underTest.createEvent(calendarEventDtoInput);
        assertThat(calendarEventDtoResponse).isNotNull();
        assertThat(calendarEventDtoResponse.getSummary()).isEqualTo(calendarEventDtoInput.getSummary());
        assertThat(calendarEventDtoResponse.getId()).isNotNull();
        assertThat(calendarEventDtoResponse.getId()).isEqualTo(id);
        assertThat(calendarEventDtoResponse.getCreated()).isEqualTo(now);
    }
}
