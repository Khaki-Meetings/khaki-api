package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TimeBlockSummaryDm {
    UUID organizationId;
    StatisticsFilterDe filterDe;
    Long totalSeconds;
    Integer meetingCount;
    Instant start;
    Instant end;
    Integer numEmployees;
    Integer numWorkdays;
    Integer totalInternalMeetingAttendees;
    Integer totalMeetingAttendees;
    Long meetingLengthSeconds;
    Integer numEmployeesOverTimeThreshold;
}
