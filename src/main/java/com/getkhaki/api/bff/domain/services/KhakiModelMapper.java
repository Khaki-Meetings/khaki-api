package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.models.CalendarEventParticipantDm;
import com.getkhaki.api.bff.domain.models.GoalDm;
import com.getkhaki.api.bff.persistence.models.GoalDao;
import com.getkhaki.api.bff.web.models.GoalMeasureDte;
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
                .description(source.getDescription())
                .recurringEventId(source.getRecurringEventId())
                .attachmentCount(0)
                .visibility(source.getVisibility())
                .build();

        List<CalendarEventParticipantDm> participants;
        val attendees = source.getAttendees();
        if (attendees == null) {
            val organizer = source.getOrganizer();
            participants = List.of(
                    new CalendarEventParticipantDm()
                            .setEmail(organizer.getEmail())
                            .setOrganizer(true)
                            .setCalendarEvent(calendarEvent)
            );
        } else {
            participants = source.getAttendees()
                    .stream()
                    .map(
                            eventAttendee -> new CalendarEventParticipantDm()
                                    .setEmail(eventAttendee.getEmail())
                                    .setOrganizer((boolean) eventAttendee.getOrDefault("organizer", false))
                                    .setCalendarEvent(calendarEvent)
                    )
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        val attachments = source.getAttachments();
        if (attachments != null) {
            calendarEvent.setAttachmentCount(attachments.size());
        }

        calendarEvent.setParticipants(participants);

        return calendarEvent;
    }

    public GoalDm mapGoalToGoalDm(GoalDao source) {

        GoalDm goalDm = this.map(source, GoalDm.class);
        switch (source.getName()) {
            case "AttendeesPerMeeting" :
                goalDm.setMeasure(GoalMeasureDte.AttendeesPerMeeting);
                break;
            case "AverageMeetingLength" :
                goalDm.setMeasure(GoalMeasureDte.AverageMeetingLength);
                break;
            case "StaffTimeInMeetings" :
                goalDm.setMeasure(GoalMeasureDte.StaffTimeInMeetings);
                break;
            case "UninterruptedTime" :
                goalDm.setMeasure(GoalMeasureDte.UninterruptedTime);
                break;
        }
        return goalDm;

    }
}
