package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceFactory;
import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalendarEventServiceUnitTests {
    private CalendarEventService underTest;
    private CalendarEventPersistenceInterface calendarEventPersistence;
    private CalendarProviderPersistenceFactory calendarProviderPersistenceFactory;

    @BeforeEach
    public void setup() {
        calendarEventPersistence = mock(CalendarEventPersistenceInterface.class);
        calendarProviderPersistenceFactory = mock(CalendarProviderPersistenceFactory.class);
        underTest = new CalendarEventService(calendarEventPersistence, calendarProviderPersistenceFactory);
    }

    @Test
    public void test() {
        CalendarEventDm calendarEventDmInput = new CalendarEventDm();
        calendarEventDmInput.setSummary("kid gloves");
        calendarEventDmInput.setCreated(Instant.now());

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


    @Test
    public void importAsynch() {
        CalendarProviderPersistenceInterface calendarProviderPersistenceInterface = mock(CalendarProviderPersistenceInterface.class);
        when(calendarProviderPersistenceFactory.get()).thenReturn(calendarProviderPersistenceInterface);
        Instant now = Instant.now();

        List<CalendarEventDm> calendarEventDmList = Lists.list(
                new CalendarEventDm()
                        .setCreated(now)
                        .setStart(now)
                        .setEnd(now.plus(1, ChronoUnit.HOURS))
        );


        Answer<Integer> answer = invocation -> {
            List<CalendarEventDm> ret = new ArrayList<>();

            calendarEventDmList.forEach(
                    calendarEventDm -> ret.add(calendarEventPersistence.createEvent(calendarEventDm))
            );
            assertThat(ret.size()).isEqualTo(calendarEventDmList.size());
            return ret.size();
        };
        when(calendarProviderPersistenceInterface.getEvents("email")).thenAnswer(answer);
//        doNothing().when(underTest).importAsync("email");
        assertThat(calendarEventDmList).isNotEmpty();


//        this.calendarProviderPersistenceFactory.get().getEvents(adminEmail);


    }
}
