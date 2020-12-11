package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.persistence.GoogleCalendarPersistenceService;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventService {
    private final CalendarEventPersistenceInterface calendarEventPersistence;
    private final CalendarProviderPersistenceFactory calendarProviderPersistenceFactory;

    @Autowired
    public CalendarEventService(CalendarEventPersistenceInterface calendarEventPersistence, CalendarProviderPersistenceFactory calendarProviderPersistenceFactory) {
        this.calendarEventPersistence = calendarEventPersistence;
        this.calendarProviderPersistenceFactory = calendarProviderPersistenceFactory;
    }

    public CalendarEventDm createEvent(CalendarEventDm calendarEventDm) {
        return calendarEventPersistence.upsert(calendarEventDm);
    }

    @Async
    public void importAsync(String adminEmail) {
        this.calendarProviderPersistenceFactory.get()
                .getEvents(adminEmail)
                .stream()
                .filter(calendarEventDm -> calendarEventDm.getParticipants().size() > 1)
                .forEach(calendarEventPersistence::upsert);
    }
}
