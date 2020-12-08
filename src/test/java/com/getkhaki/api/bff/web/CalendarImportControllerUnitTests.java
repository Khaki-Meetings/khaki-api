package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.CalendarEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;

public class CalendarImportControllerUnitTests {
    private CalendarEventService calendarEventService;
    private CalendarImportController underTest;

    @BeforeEach
    public void setup() {
        calendarEventService = mock(CalendarEventService.class);
        underTest = new CalendarImportController(calendarEventService);
    }

    @Test
    public void importAsync() {
    }
}
