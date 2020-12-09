package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.models.CalendarEventParticipantDm;
import com.getkhaki.api.bff.domain.models.DomainDm;
import com.getkhaki.api.bff.domain.models.EmailDm;
import com.google.api.services.calendar.model.Event;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class KhakiModelMapper extends ModelMapper {
    public CalendarEventDm mapEventToCalendarEventDm(Event source) {
        val calendarEvent = CalendarEventDm.builder()
                .googleCalendarId(source.getId())
                .summary(source.getSummary() != null ? source.getSummary() : "")
                .created(Instant.ofEpochMilli(source.getCreated().getValue()))
                .start(Instant.ofEpochMilli(source.getStart().getDateTime().getValue()))
                .end(Instant.ofEpochMilli(source.getEnd().getDateTime().getValue()))
                .build();

        List<CalendarEventParticipantDm> participants;
        val attendees = source.getAttendees();
        if (attendees == null) {
            val organizer = source.getOrganizer();
            String[] parts = organizer.getEmail().split("@");
            if (parts.length != 2) {
                return calendarEvent;
            }
            participants = List.of(
                    new CalendarEventParticipantDm()
                            .setEmail(
                                    new EmailDm()
                                            .setUser(parts[0])
                                            .setDomain(new DomainDm().setName(parts[1]))
                            )
                            .setOrganizer(true)
                            .setCalendarEvent(calendarEvent)
            );
        } else {
            participants = source.getAttendees()
                    .stream()
                    .map(
                            eventAttendee -> {
                                String[] parts = eventAttendee.getEmail().split("@");
                                if (parts.length != 2) {
                                    return null;
                                }
                                return new CalendarEventParticipantDm()
                                        .setEmail(
                                                new EmailDm()
                                                        .setUser(parts[0])
                                                        .setDomain(new DomainDm().setName(parts[1]))
                                        )
                                        .setOrganizer((boolean) eventAttendee.getOrDefault("organizer", false))
                                        .setCalendarEvent(calendarEvent);
                            }
                    )
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        calendarEvent.setParticipants(participants);

        return calendarEvent;
    }
}
