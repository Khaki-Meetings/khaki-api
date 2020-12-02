package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.views.DepartmentStatisticsView;
import com.getkhaki.api.bff.persistence.repositories.DepartmentStatisticsRepositoryInterface;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import org.apache.commons.lang3.NotImplementedException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class DepartmentStatisticsPersistenceService implements DepartmentStatisticsPersistenceInterface {

    private final ModelMapper modelMapper;
    private DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface;

    public DepartmentStatisticsPersistenceService(ModelMapper modelMapper, DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface) {
        this.modelMapper = modelMapper;
        this.departmentStatisticsRepositoryInterface = departmentStatisticsRepositoryInterface;
    }

    @Override
    public List<DepartmentStatisticsDm> getPerDepartmentStatistics(Instant start, Instant end) {
        UUID tenantId = UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4");
        List<DepartmentStatisticsView> daos = departmentStatisticsRepositoryInterface.findAllDepartmentStatisticsInRange(
                start,
                end,
                tenantId
        );

        List<DepartmentStatisticsDm> dms = modelMapper.map(
                daos,
                new TypeToken<List<DepartmentStatisticsResponseDto>>() {
                }.getType()
        );
        return dms;
    }
}
