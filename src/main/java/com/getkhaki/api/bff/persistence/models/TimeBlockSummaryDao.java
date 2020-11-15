package com.getkhaki.api.bff.persistence.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TimeBlockSummaryDao {
    @Enumerated(EnumType.STRING)
    IntervalEnumDao interval;
    long totalTime;
    long totalCost;
    long averageCost;
    long meetingCount;
}
