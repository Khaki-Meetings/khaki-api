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
public class GoalDto {
    UUID id;
    GoalMeasureDte measure;
    Integer greaterThanOrEqualTo;
    Integer lessThanOrEqualTo;
    String departmentName;
}
