package com.getkhaki.api.bff.persistance;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventPersistenceService implements CalendarEventPersistenceInterface {
    @Override
    public CalendarEventDm createEvent(CalendarEventDm calendarEventDm) {
        return null;
    }
}
