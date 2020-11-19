package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceFactory;
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
        return calendarEventPersistence.createEvent(calendarEventDm);
    }


    @Async("asyncExecutor")
    public void importAsync(String adminEmail) {

//        this.calendarProviderPersistenceFactory.get().getEvents(adminEmail);

    }

}
