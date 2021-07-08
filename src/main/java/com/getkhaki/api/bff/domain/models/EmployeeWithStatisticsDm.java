package com.getkhaki.api.bff.domain.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class EmployeeWithStatisticsDm {
    UUID id;
    String email;
    String firstName;
    String lastName;
    String avatarUrl;
    Integer totalMeetings;
    Long totalSeconds;
    String department;
}
