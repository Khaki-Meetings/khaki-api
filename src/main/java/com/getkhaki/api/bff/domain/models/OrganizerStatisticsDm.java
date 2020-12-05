package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrganizerStatisticsDm {
    String organizerEmail;
    String organizerFirstName;
    String organizerLastName;
    int totalMeetings;
    Double totalCost;
    int totalHours;
}
