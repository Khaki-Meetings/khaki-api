package com.getkhaki.api.bff.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TimeBlockSummaryResponseDto {
    Long totalSeconds;
    Integer meetingCount;
    Instant start;
    Instant end;
    Integer numEmployees;
    Integer numWorkdays;
    Integer totalMeetingInternalAttendees;
    Integer totalMeetingAttendees;
    Long meetingLengthSeconds;
    Integer numEmployeesOverTimeThreshold;
}
