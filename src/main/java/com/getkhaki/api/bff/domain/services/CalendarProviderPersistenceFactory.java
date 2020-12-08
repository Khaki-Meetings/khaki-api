package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;
import com.getkhaki.api.bff.persistence.GoogleCalendarPersistenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarProviderPersistenceFactory {
    private final GoogleCalendarPersistenceService googleCalendarPersistenceService;

    @Autowired
    public CalendarProviderPersistenceFactory(GoogleCalendarPersistenceService googleCalendarPersistenceService) {
        this.googleCalendarPersistenceService = googleCalendarPersistenceService;
    }

    public CalendarProviderPersistenceInterface get() {
        return this.googleCalendarPersistenceService;
    }
}
