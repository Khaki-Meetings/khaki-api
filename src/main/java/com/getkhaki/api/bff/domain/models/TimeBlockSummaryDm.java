package com.getkhaki.api.bff.domain.models;

import com.getkhaki.api.bff.persistence.models.IntervalEnumDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class TimeBlockSummaryDm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @Enumerated(EnumType.STRING)
    IntervalEnumDto interval;
    long totalTime;
    long totalCost;
    long averageCost;
    long meetingCount;
}
