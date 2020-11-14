package com.getkhaki.api.bff.web.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class OrganizerStatisticsResponseDto {
    OrganizerDto organizer;
    int totalMeetings;
    int totalHours;
    int totalCost;
}
