package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.persistence.models.DepartmentStatisticsDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.UUID;

public interface DepartmentStatisticsRepositoryInterface extends JpaRepository<DepartmentStatisticsDao, UUID> {
    @Query("")
    DepartmentStatisticsDao findDepartmentStatisticsInRange(ZonedDateTime start, ZonedDateTime end);
}
