package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.services.CalendarEventService;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CalendarImportControllerUnitTests {
    private CalendarImportController underTest;

    private CalendarEventService calendarEventService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        calendarEventService = mock(CalendarEventService.class);
        modelMapper = mock(ModelMapper.class);
        underTest = new CalendarImportController(calendarEventService, modelMapper);
    }


    @Test
    public void importAsynch() {
        String email="email";
        doNothing().when(underTest).importAsync(email);
        verify(calendarEventService, times(1)).importAsync(email);

    }
}
