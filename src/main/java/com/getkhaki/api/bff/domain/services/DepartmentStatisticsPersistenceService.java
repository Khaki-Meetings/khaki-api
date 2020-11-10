package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.DepartmentStatisticsRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class DepartmentStatisticsPersistenceService implements DepartmentStatisticsPersistenceInterface {

    private final ModelMapper modelMapper;

    public DepartmentStatisticsPersistenceService(ModelMapper modelMapper,DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface) {
        this.modelMapper = modelMapper;
        this.departmentStatisticsRepositoryInterface=departmentStatisticsRepositoryInterface;
    }

    private DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface;

    @Override
    public DepartmentStatisticsDm getPerDepartmentStatistics(ZonedDateTime start, ZonedDateTime end) {
        return modelMapper.map( departmentStatisticsRepositoryInterface.findDepartmentStatisticsInRange(start,end), DepartmentStatisticsDm.class);
    }
}
