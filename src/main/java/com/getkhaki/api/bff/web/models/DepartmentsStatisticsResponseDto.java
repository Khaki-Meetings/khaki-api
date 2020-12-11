package com.getkhaki.api.bff.web.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DepartmentsStatisticsResponseDto {
    List<DepartmentStatisticsResponseDto> departmentsStatistics;
}
