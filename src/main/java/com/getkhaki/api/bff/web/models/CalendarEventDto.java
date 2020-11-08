package com.getkhaki.api.bff.web.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEventDto {
    String id;
    String summary;
    LocalDateTime created;
}
