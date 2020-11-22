package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;

import java.util.List;

public class GoogleCalendarPersistenceService implements CalendarProviderPersistenceInterface {
    @Override
    public List<CalendarEventDm> getEvents(String email) {
        return null;
    }
}
