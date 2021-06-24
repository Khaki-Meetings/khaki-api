package com.getkhaki.api.bff.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class EmployeeWithStatisticsDm {
    String email;
    String firstName;
    String lastName;
    String avatarUrl;
    Integer totalMeetings;
    Long totalSeconds;
    String department;
}
