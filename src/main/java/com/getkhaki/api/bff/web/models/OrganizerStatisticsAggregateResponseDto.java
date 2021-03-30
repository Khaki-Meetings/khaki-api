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
public class OrganizerStatisticsAggregateResponseDto {
    String organizerId;
    String organizerEmail;
    String organizerFirstName;
    String organizerLastName;
    String organizerAvatarUrl;
    Integer internalMeetingCount;
    Integer internalMeetingSeconds;
    Integer externalMeetingCount;
    Integer externalMeetingSeconds;
}