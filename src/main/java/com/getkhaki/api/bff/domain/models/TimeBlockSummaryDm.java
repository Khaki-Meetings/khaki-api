package com.getkhaki.api.bff.domain.models;

import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;
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
public class TimeBlockSummaryDm {
    UUID id;
    IntervalEnumDm interval;
    long totalTime;
    long totalCost;
    long averageCost;
    long meetingCount;
}
