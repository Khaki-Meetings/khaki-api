package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.DepartmentStatisticsRepositoryInterface;
import org.apache.commons.lang3.NotImplementedException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class DepartmentStatisticsPersistenceService implements DepartmentStatisticsPersistenceInterface {

    private final ModelMapper modelMapper;
    private DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface;

    public DepartmentStatisticsPersistenceService(ModelMapper modelMapper, DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface) {
        this.modelMapper = modelMapper;
        this.departmentStatisticsRepositoryInterface = departmentStatisticsRepositoryInterface;
    }

    @Override
    public List<DepartmentStatisticsDm> getPerDepartmentStatistics(ZonedDateTime start, ZonedDateTime end) {
//        DepartmentStatisticsDao dao = departmentStatisticsRepositoryInterface.findDepartmentStatisticsInRange(start, end);
//        DepartmentStatisticsDm dm = modelMapper.map(dao, DepartmentStatisticsDm.class);
//        return dm;
        throw new NotImplementedException();
    }
}
