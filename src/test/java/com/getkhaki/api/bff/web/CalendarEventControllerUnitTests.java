package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.*;
import com.getkhaki.api.bff.domain.services.CalendarEventService;
import com.getkhaki.api.bff.web.models.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
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
        Instant now = Instant.now();
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

    @Test
    public void getMeetingList() {
        var employeeId = UUID.randomUUID().toString();
        var start = Instant.now();
        var end = Instant.now();

        var externalFilterDte = Optional.of(StatisticsFilterDte.External);
        var internalFilterDte = Optional.of(StatisticsFilterDte.Internal);

        var externalFilterDe = StatisticsFilterDe.External;
        var internalFilterDe = StatisticsFilterDe.Internal;

        var externalCalendarEventsResponseDto = mock(CalendarEventsWithAttendeesResponseDto.class);
        var internalCalendarEventsResponseDto = mock(CalendarEventsWithAttendeesResponseDto.class);

        var externalCalendarEventDetailDm = mock(CalendarEventDetailDm.class);
        var internalCalendarEventDetailDm = mock(CalendarEventDetailDm.class);

        CalendarEventDm calendarEventDmMapped = new CalendarEventDm();
        calendarEventDmMapped.setCreated(Instant.now());
        calendarEventDmMapped.setSummary("test");

        Pageable pageable = PageRequest.of(0, 2);

        PageImpl<CalendarEventDetailDm> externalDms = new PageImpl<>(Lists.list(externalCalendarEventDetailDm));
        PageImpl<CalendarEventDetailDm> internalDms = new PageImpl<>(Lists.list(internalCalendarEventDetailDm));

        PageImpl<CalendarEventsWithAttendeesResponseDto> externalDtos = new PageImpl<>(Lists.list(externalCalendarEventsResponseDto));
        PageImpl<CalendarEventsWithAttendeesResponseDto> internalDtos = new PageImpl<>(Lists.list(internalCalendarEventsResponseDto));

        when(modelMapper.map(StatisticsFilterDte.External, StatisticsFilterDe.class))
                .thenReturn(externalFilterDe);

        when(modelMapper.map(StatisticsFilterDte.Internal, StatisticsFilterDe.class))
                .thenReturn(internalFilterDe);

        when(modelMapper.map(externalCalendarEventDetailDm, CalendarEventDm.class)).thenReturn(calendarEventDmMapped);

        when(modelMapper.map(internalCalendarEventDetailDm, CalendarEventDm.class)).thenReturn(calendarEventDmMapped);

        when(calendarEventService.getCalendarEvents(start, end, employeeId, externalFilterDe, pageable))
                .thenReturn(externalDms);

        when(calendarEventService.getCalendarEvents(start, end, employeeId, internalFilterDe, pageable))
                .thenReturn(internalDms);

        when(modelMapper.map(externalCalendarEventDetailDm, CalendarEventsWithAttendeesResponseDto.class))
                .thenReturn(externalCalendarEventsResponseDto);

        when(modelMapper.map(internalCalendarEventDetailDm, CalendarEventsWithAttendeesResponseDto.class))
                .thenReturn(internalCalendarEventsResponseDto);

        Page<CalendarEventsWithAttendeesResponseDto> externalResult = this.underTest.getCalendarEvents(
                start, end, employeeId, externalFilterDte, pageable);

        assertThat(externalResult).isEqualTo(externalDtos);

        Page<CalendarEventsWithAttendeesResponseDto> internalResult = this.underTest.getCalendarEvents(
                start, end, employeeId, internalFilterDte, pageable);

        assertThat(internalResult).isEqualTo(internalDtos);
    }

}
