package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.CalendarEventService;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class CalendarEventControllerTests {
    private CalendarEventController underTest;

    private CalendarEventService calendarEventService;

    @BeforeEach
    public void setup() {
        calendarEventService = mock(CalendarEventService.class);
        underTest = new CalendarEventController(calendarEventService);
    }
    @Test
    public void goodPath() {
        CalendarEventDto calendarEventDto = underTest.createEvent();
        assertThat(calendarEventDto).isNotNull();
    }
}
