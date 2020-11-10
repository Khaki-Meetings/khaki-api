package com.getkhaki.api.bff.web.models;

import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TimeBlockSummaryResponseDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @Enumerated(EnumType.STRING)
    IntervalEnumDao interval;
    long totalTime;
    long totalCost;
    long averageCost;
    long meetingCount;
}
