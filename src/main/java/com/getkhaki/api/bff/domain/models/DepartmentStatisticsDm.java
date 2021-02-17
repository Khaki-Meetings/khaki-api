package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DepartmentStatisticsDm {
    UUID id;
    String department;
    Long meetingCount;
    Long totalSeconds;
    Long totalCost;
    Long averageCost;
    Long inventorySecondsAvailable;
}
