package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class DepartmentStatisticsPersistenceService implements DepartmentStatisticsPersistenceInterface {
    @Override
    public DepartmentStatisticsDm getPerDepartmentStatistics(ZonedDateTime start, ZonedDateTime end) {
        return null;
    }
}
