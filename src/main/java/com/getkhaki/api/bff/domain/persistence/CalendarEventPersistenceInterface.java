package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDetailDm;
import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.models.CalendarEventsEmployeeTimeDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface CalendarEventPersistenceInterface {
    CalendarEventDm upsert(CalendarEventDm calendarEventDm);

    Page<CalendarEventDetailDm> getCalendarEvents(
            Instant sDate, Instant eDate, String organizer,
            String attendee, StatisticsFilterDe filterDe, Pageable pageable);

}
