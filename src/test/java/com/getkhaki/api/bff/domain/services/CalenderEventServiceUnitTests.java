package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

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
        calendarEventDmInput.summary("kid gloves");
        calendarEventDmInput.created(LocalDateTime.now());

        UUID id = UUID.randomUUID();
        CalendarEventDm calendarEventDmResponse = new CalendarEventDm(
                id,
                calendarEventDmInput.summary(),
                calendarEventDmInput.created()
        );

        when(calendarEventPersistence.createEvent(calendarEventDmInput)).thenReturn(calendarEventDmResponse);

        CalendarEventDm ret = underTest.createEvent(calendarEventDmInput);
        assertThat(ret).isNotNull();
        assertThat(ret.id()).isEqualTo(id);
        assertThat(ret.summary()).isEqualTo(calendarEventDmResponse.summary());
        assertThat(ret.created()).isEqualTo(calendarEventDmResponse.created());
    }
}
