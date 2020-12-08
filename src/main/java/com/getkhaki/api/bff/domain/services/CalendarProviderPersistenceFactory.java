package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;
import com.getkhaki.api.bff.persistence.GoogleCalendarPersistenceService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class CalendarProviderPersistenceFactory {
    private final GoogleCalendarPersistenceService googleCalendarPersistenceService;

    @Inject
    public CalendarProviderPersistenceFactory(GoogleCalendarPersistenceService googleCalendarPersistenceService) {
        this.googleCalendarPersistenceService = googleCalendarPersistenceService;
    }

    public CalendarProviderPersistenceInterface get() {
        return this.googleCalendarPersistenceService;
    }
}
