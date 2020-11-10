package com.getkhaki.api.bff.web.models;

import com.getkhaki.api.bff.persistence.models.IntervalEnumDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class TimeBlockSummaryResponseDto {
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
