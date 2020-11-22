package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;

import java.time.ZonedDateTime;
import java.util.List;

public interface DepartmentStatisticsPersistenceInterface {
    List<DepartmentStatisticsDm> getPerDepartmentStatistics(ZonedDateTime start, ZonedDateTime end);
}
