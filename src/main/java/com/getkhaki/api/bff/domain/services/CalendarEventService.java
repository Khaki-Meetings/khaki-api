package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.PersonPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsWithAttendeesViewInterface;
import com.getkhaki.api.bff.web.models.CalendarEventsWithAttendeesResponseDto;
import com.getkhaki.api.bff.web.models.PersonDto;
import lombok.extern.apachecommons.CommonsLog;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
            calendarEventPersistence.upsert(event);
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
        log.info("RUNNING IMPORT");
        val timeAgo = Instant.now().minus(importCronMinutes, ChronoUnit.MINUTES);
        organizationPersistenceService
                .getImportEnabledOrganizations()
                .forEach(
                        organizationDm -> importAsync(organizationDm.getAdminEmail(), timeAgo)
                );
    }

    public static String getGuidFromByteArray(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for(int i=0; i<bytes.length; i++) {
            buffer.append(String.format("%02x", bytes[i]));
        }
        return buffer.toString();
    }

    public Page<CalendarEventsWithAttendeesResponseDto> getCalendarEventsAttendees(Instant sDate, Instant eDate, Pageable pageable) {

        Page<CalendarEventsWithAttendeesViewInterface> page = calendarEventPersistence.getCalendarEventsAttendees(sDate, eDate, pageable);

        List<CalendarEventsWithAttendeesResponseDto> newContent = new ArrayList<CalendarEventsWithAttendeesResponseDto>();

        for (CalendarEventsWithAttendeesViewInterface event : page.getContent()) {
            CalendarEventsWithAttendeesResponseDto dto = new CalendarEventsWithAttendeesResponseDto();
            String eventId = getGuidFromByteArray(event.getId());
            dto.setId(eventId);
            dto.setGoogleCalendarId(event.getGoogleCalendarId());
            dto.setSummary(event.getSummary());
            dto.setCreated(event.getCreated());
            dto.setStart(event.getStart());
            dto.setEnd(event.getEnd());
            PersonDm organizer = personPersistenceService.getOrganizerByCalendarEvent(eventId);
            if (organizer != null) {
                dto.setOrganizer(this.modelMapper.map(organizer, PersonDto.class));
            }
            dto.setParticipants(
                    personPersistenceService.getPersonsByCalendarEvent(eventId)
                        .stream()
                        .map(person -> modelMapper.map(person, PersonDto.class))
                        .collect(Collectors.toList())
            );
            newContent.add(dto);
        }

        return new PageImpl<CalendarEventsWithAttendeesResponseDto>(
                newContent,
                pageable,
                page.getTotalElements());
    }
}
