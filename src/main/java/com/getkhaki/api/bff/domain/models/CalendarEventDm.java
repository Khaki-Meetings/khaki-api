package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CalendarEventDm {
    UUID id;

    String googleCalendarId;

    String summary;

    ZonedDateTime created;

    ZonedDateTime start;

    ZonedDateTime end;
}
