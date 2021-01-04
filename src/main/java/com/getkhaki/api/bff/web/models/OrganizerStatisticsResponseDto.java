package com.getkhaki.api.bff.web.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
public class OrganizerStatisticsResponseDto {
    String organizerEmail;
    String organizerFirstName;
    String organizerLastName;
    Integer totalMeetings;
    Long totalSeconds;
    Double totalCost;
}
