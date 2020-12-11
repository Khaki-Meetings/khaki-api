package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
public class CalendarEventParticipantDm {
    UUID id;
    CalendarEventDm calendarEvent;
    String email;
    boolean organizer;
}
