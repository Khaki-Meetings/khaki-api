package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;

public interface CalendarProviderPersistenceInterface {
    List<CalendarEventDm> getEvents(String email, Instant timeAgo);
}
