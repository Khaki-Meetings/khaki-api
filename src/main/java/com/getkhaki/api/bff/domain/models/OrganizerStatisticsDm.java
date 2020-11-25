package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class OrganizerStatisticsDm {
    String organizerEmail;
    int totalMeetingCount;
    Double totalCost;
    int totalHours;
}
