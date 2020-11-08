package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEventDm {
    String id;
    String summary;
    LocalDateTime created;
}
