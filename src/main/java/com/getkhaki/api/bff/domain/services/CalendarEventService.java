package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventService {
    private final CalendarEventPersistenceInterface calendarEventPersistence;
    private final CalendarProviderPersistenceFactory calendarProviderPersistenceFactory;
    private final OrganizationPersistenceInterface organizationPersistenceService;

    @Autowired
    public CalendarEventService(
            CalendarEventPersistenceInterface calendarEventPersistence,
            CalendarProviderPersistenceFactory calendarProviderPersistenceFactory,
            OrganizationPersistenceInterface organizationPersistenceService
    ) {
        this.calendarEventPersistence = calendarEventPersistence;
        this.calendarProviderPersistenceFactory = calendarProviderPersistenceFactory;
        this.organizationPersistenceService = organizationPersistenceService;
    }

    public CalendarEventDm createEvent(CalendarEventDm calendarEventDm) {
        return calendarEventPersistence.upsert(calendarEventDm);
    }

    @Async
    public void importAsync(String adminEmail) {
        this.calendarProviderPersistenceFactory.get()
                .getEvents(adminEmail)
                .stream()
                .filter(calendarEventDm -> calendarEventDm.getParticipants().size() > 1)
                .forEach(calendarEventPersistence::upsert);
    }

    public void importCron() {
        organizationPersistenceService
                .getImportEnabledOrganizations()
                .forEach(
                        organizationDm -> importAsync(organizationDm.getAdminEmail())
                );
    }
}
