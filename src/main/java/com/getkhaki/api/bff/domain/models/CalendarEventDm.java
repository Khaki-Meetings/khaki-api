package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
public class CalendarEventDm {
    UUID id;

    String googleCalendarId;

    String summary;

    Instant created;

    Instant start;

    Instant end;

    @Builder.Default
    List<CalendarEventParticipantDm> participants = new ArrayList<>();
}
