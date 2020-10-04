package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalenderEventServiceUnitTests {
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
        calendarEventDmInput.setCreated(LocalDateTime.now());

        String id = "1001001";
        CalendarEventDm calendarEventDmResponse = new CalendarEventDm(
                id,
                calendarEventDmInput.getSummary(),
                calendarEventDmInput.getCreated()
        );

        when(calendarEventPersistence.createEvent(calendarEventDmInput)).thenReturn(calendarEventDmResponse);

        CalendarEventDm ret = underTest.createEvent(calendarEventDmInput);
        assertThat(ret).isNotNull();
        assertThat(ret.getId()).isEqualTo(id);
        assertThat(ret.getSummary()).isEqualTo(calendarEventDmResponse.getSummary());
        assertThat(ret.getCreated()).isEqualTo(calendarEventDmResponse.getCreated());
    }
}
