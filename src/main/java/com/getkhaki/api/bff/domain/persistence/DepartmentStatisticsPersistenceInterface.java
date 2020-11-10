package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;

import java.time.ZonedDateTime;

public interface DepartmentStatisticsPersistenceInterface {
    DepartmentStatisticsDm getPerDepartmentStatistics(ZonedDateTime start, ZonedDateTime end);
}
