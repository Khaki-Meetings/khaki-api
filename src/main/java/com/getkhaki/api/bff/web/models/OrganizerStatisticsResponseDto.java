package com.getkhaki.api.bff.web.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class OrganizerStatisticsResponseDto {
    OrganizerDto organizer;
    int totalMeetings;
    int totalHours;
    int totalCost;
}
