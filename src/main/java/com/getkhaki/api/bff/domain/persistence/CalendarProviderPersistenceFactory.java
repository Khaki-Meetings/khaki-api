package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.persistence.GoogleCalendarPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class CalendarProviderPersistenceFactory {
    public CalendarProviderPersistenceInterface get() {
        return new GoogleCalendarPersistenceService();
    }
}
