package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventService {
    private final CalendarEventPersistenceInterface calendarEventPersistence;

    @Autowired
    public CalendarEventService(CalendarEventPersistenceInterface calendarEventPersistence) {
        this.calendarEventPersistence = calendarEventPersistence;
    }

    public CalendarEventDm createEvent(CalendarEventDm calendarEventDm) {
        return calendarEventPersistence.createEvent(calendarEventDm);
    }
}
