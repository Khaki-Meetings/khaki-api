package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import lombok.extern.apachecommons.CommonsLog;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@CommonsLog
@Service
public class CalendarEventService {
    private final CalendarEventPersistenceInterface calendarEventPersistence;
    private final CalendarProviderPersistenceFactory calendarProviderPersistenceFactory;
    private final OrganizationPersistenceInterface organizationPersistenceService;

    @Value("${com.getkhaki.api.bff.calendar-cron-import-history-minutes:360}")
    private int importCronMinutes;

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
    public void importAsync(String adminEmail, Instant timeAgo) {
        this.calendarProviderPersistenceFactory.get()
                .getEvents(adminEmail, timeAgo)
                .stream()
                .filter(calendarEventDm -> calendarEventDm.getParticipants().size() > 1)
                .forEach(calendarEventPersistence::upsert);
    }

//    @Scheduled(fixedDelay = 3600000)
    public void importCron() {
        log.info("RUNNING IMPORT");
        val timeAgo = Instant.now().minus(importCronMinutes, ChronoUnit.MINUTES);
        organizationPersistenceService
                .getImportEnabledOrganizations()
                .forEach(
                        organizationDm -> importAsync(organizationDm.getAdminEmail(), timeAgo)
                );
    }
}
