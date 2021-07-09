package com.getkhaki.api.bff.web.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class EmployeeWithStatisticsDto {
    UUID id;
    String email;
    String firstName;
    String lastName;
    String avatarUrl;
    String department;
    Integer totalMeetings;
    Long totalSeconds;
}

