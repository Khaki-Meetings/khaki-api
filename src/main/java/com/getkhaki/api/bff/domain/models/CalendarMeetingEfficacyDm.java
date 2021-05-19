package com.getkhaki.api.bff.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
public class CalendarMeetingEfficacyDm {
    Integer averageMeetingLength;
    Integer averageStaffTimePerMeeting;
}
