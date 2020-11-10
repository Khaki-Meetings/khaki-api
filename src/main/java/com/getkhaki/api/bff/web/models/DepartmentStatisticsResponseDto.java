package com.getkhaki.api.bff.web.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;


@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class DepartmentStatisticsResponseDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    String department;
    long meetingCount;
    long totalHours;
    long totalCost;
    long averageCost;
}
