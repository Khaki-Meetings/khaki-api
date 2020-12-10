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
    Logger logger = LoggerFactory.getLogger(GoogleCalendarPersistenceService.class);

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
        val events = this.calendarProviderPersistenceFactory.get()
                .getEvents(adminEmail);
//                .forEach(calendarEventPersistence::createEvent);
//        events.forEach(calendarEventDm -> logger.debug(calendarEventDm.toString()));
        events.forEach(calendarEventPersistence::upsert);
    }
}
