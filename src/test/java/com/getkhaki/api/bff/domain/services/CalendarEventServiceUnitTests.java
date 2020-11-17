package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalendarEventServiceUnitTests {
    private CalendarEventService underTest;
    private CalendarEventPersistenceInterface calendarEventPersistence;

    @BeforeEach
    public void setup() {
        calendarEventPersistence = mock(CalendarEventPersistenceInterface.class);
        underTest = new CalendarEventService(calendarEventPersistence);
    }

    @Test
    public void test() {
        CalendarEventDm calendarEventDmInput = new CalendarEventDm();
        calendarEventDmInput.setSummary("kid gloves");
        calendarEventDmInput.setCreated(ZonedDateTime.now());

        UUID id = UUID.randomUUID();
        CalendarEventDm calendarEventDmResponse = new CalendarEventDm()
                .setSummary(calendarEventDmInput.getSummary())
                .setGoogleCalendarId(calendarEventDmInput.getGoogleCalendarId())
                .setCreated(calendarEventDmInput.getCreated())
                .setId(id);

        when(calendarEventPersistence.createEvent(calendarEventDmInput)).thenReturn(calendarEventDmResponse);

        CalendarEventDm ret = underTest.createEvent(calendarEventDmInput);
        assertThat(ret).isNotNull();
        assertThat(ret.getId()).isEqualTo(id);
        assertThat(ret.getSummary()).isEqualTo(calendarEventDmResponse.getSummary());
        assertThat(ret.getCreated()).isEqualTo(calendarEventDmResponse.getCreated());
    }
}
