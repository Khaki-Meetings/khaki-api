package com.getkhaki.api.bff.web.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TimeBlockSummaryResponseDto {
    UUID id;
    IntervalEnumDto interval;
    long totalTime;
    long totalCost;
    long averageCost;
    long meetingCount;
}
