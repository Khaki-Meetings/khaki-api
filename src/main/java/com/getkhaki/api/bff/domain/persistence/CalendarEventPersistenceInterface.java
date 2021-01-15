package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsWithAttendeesViewInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CalendarEventPersistenceInterface {
    CalendarEventDm upsert(CalendarEventDm calendarEventDm);

    Page<CalendarEventsWithAttendeesViewInterface> getCalendarEventsAttendees(Pageable pageable);
}
