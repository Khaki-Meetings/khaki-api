package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DepartmentStatisticsDm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    String department;
    long meetingCount;
    long totalHours;
    long totalCost;
    long averageCost;
}
