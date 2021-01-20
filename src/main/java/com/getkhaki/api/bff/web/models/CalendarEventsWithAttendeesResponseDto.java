package com.getkhaki.api.bff.web.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
public class CalendarEventsWithAttendeesResponseDto {
    String id;
    String googleCalendarId;
    String summary;
    Instant created;
    Instant start;
    Instant end;
    List<PersonDto> participants;
    PersonDto organizer;
    Integer numberInternalAttendees;
    Integer totalSeconds;
}
