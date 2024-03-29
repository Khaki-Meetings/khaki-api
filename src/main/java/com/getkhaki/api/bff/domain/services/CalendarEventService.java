package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDetailDm;
import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.PersonPersistenceInterface;
import lombok.extern.apachecommons.CommonsLog;
import lombok.val;
import org.h2.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

@CommonsLog
@Service
public class CalendarEventService {
    private final CalendarEventPersistenceInterface calendarEventPersistence;
    private final CalendarProviderPersistenceFactory calendarProviderPersistenceFactory;
    private final OrganizationPersistenceInterface organizationPersistenceService;
    private final PersonPersistenceInterface personPersistenceService;
    private final ModelMapper modelMapper;

    @Value("${com.getkhaki.api.bff.calendar-cron-import-history-minutes:360}")
    private int importCronMinutes;

    public CalendarEventService(
            CalendarEventPersistenceInterface calendarEventPersistence,
            CalendarProviderPersistenceFactory calendarProviderPersistenceFactory,
            OrganizationPersistenceInterface organizationPersistenceService,
            PersonPersistenceInterface personPersistenceService,
            ModelMapper modelMapper
    ) {
        this.calendarEventPersistence = calendarEventPersistence;
        this.calendarProviderPersistenceFactory = calendarProviderPersistenceFactory;
        this.organizationPersistenceService = organizationPersistenceService;
        this.personPersistenceService = personPersistenceService;
        this.modelMapper = modelMapper;
    }

    public CalendarEventDm createEvent(CalendarEventDm calendarEventDm) {
        return calendarEventPersistence.upsert(calendarEventDm);
    }

    @Async
    public void importAsync(String adminEmail, Instant timeAgo) {
        log.info(
                String.format(
                        "import started at %s for %s, back to %s",
                        Instant.now(),
                        adminEmail,
                        timeAgo
                )
        );

        val filteredEvents = this.calendarProviderPersistenceFactory.get()
                .getEvents(adminEmail, timeAgo)
                .stream()
                .filter(calendarEventDm -> calendarEventDm.getParticipants().size() > 1);
        
        AtomicInteger count = new AtomicInteger();
        filteredEvents.forEach(event -> {
            count.getAndIncrement();
            try {
                calendarEventPersistence.upsert(event);
            } catch (Exception e) {
                log.error("Caught an exception during " + adminEmail + " import: " + e.getMessage());
            }
        });
        log.info(
                String.format(
                        "import completed at %s for %s, back to %s. Upserted %s",
                        Instant.now(),
                        adminEmail,
                        timeAgo.toString(),
                        count
                )
        );
    }

    @Scheduled(fixedDelay = 3600000)
    public void importCron() {
        log.info("Starting import cronjob");
        val timeAgo = Instant.now().minus(importCronMinutes, ChronoUnit.MINUTES);
        organizationPersistenceService
                .getImportEnabledOrganizations()
                .forEach(
                    organizationDm -> {
                        log.info("Importing " + organizationDm.getName() + " with admin email " + organizationDm.getAdminEmail());
                        try {
                            importAsync(organizationDm.getAdminEmail(), timeAgo);
                        } catch (Exception e) {
                            log.error("Caught an exception during " + organizationDm.getName() + " import: " + e.getMessage());
                        }
                    }
                );
    }

    public static String getGuidFromByteArray(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (i == 4 || i == 6 || i == 8 || i == 10) {
                buffer.append("-");
            }
            buffer.append(String.format("%02x", bytes[i]));
        }
        return buffer.toString();
    }

    public Page<CalendarEventDetailDm> getCalendarEvents(
            Instant start,
            Instant end,
            String organizer,
            String attendee,
            StatisticsFilterDe filter,
            Pageable pageable) {

            Page<CalendarEventDetailDm> calendarEventsWithAttendeesDmList = calendarEventPersistence
                    .getCalendarEvents(start, end, organizer, attendee, filter, pageable);
            return calendarEventsWithAttendeesDmList;
    }
}