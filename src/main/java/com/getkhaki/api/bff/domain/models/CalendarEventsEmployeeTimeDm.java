package com.getkhaki.api.bff.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
public class CalendarEventsEmployeeTimeDm {

    Integer numOverThreshold;

    Integer numEmployees;

}
