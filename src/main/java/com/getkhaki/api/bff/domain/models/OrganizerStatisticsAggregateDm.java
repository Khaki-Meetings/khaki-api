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
public class OrganizerStatisticsAggregateDm {
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
