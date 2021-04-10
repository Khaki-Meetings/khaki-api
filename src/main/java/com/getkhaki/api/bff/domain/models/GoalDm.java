package com.getkhaki.api.bff.domain.models;

import com.getkhaki.api.bff.web.models.GoalMeasureDte;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = true)
@SuperBuilder

public class GoalDm {
    UUID id;
    GoalMeasureDte measure;
    Integer greaterThanOrEqualTo;
    Integer lessThanOrEqualTo;
    String departmentName;
}
