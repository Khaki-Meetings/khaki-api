package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsWithAttendeesView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CalendarEventPersistenceInterface {
    CalendarEventDm upsert(CalendarEventDm calendarEventDm);

    Page<CalendarEventsWithAttendeesView> getCalendarEventsAttendees(Pageable pageable);
}
