package com.getkhaki.api.bff.web.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CalendarEventDto {
    private String id;
    private LocalDateTime created;
    private String summary;
}
