package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;

import java.time.Instant;
import java.util.List;

public interface DepartmentStatisticsPersistenceInterface {
    List<DepartmentStatisticsDm> getPerDepartmentStatistics(Instant start, Instant end, StatisticsFilterDe filterDe);
}
