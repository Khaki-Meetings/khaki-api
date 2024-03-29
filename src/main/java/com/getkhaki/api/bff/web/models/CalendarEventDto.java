package com.getkhaki.api.bff.web.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CalendarEventDto {
    UUID id;

    String googleCalendarId;

    String summary;

    Instant created;

    Instant start;

    Instant end;

}
