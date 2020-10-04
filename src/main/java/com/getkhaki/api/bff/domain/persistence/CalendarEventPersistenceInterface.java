package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;

public interface CalendarEventPersistenceInterface {
    CalendarEventDm createEvent(CalendarEventDm calendarEventDm);
}
