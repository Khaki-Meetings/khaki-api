package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.views.DepartmentStatisticsView;
import com.getkhaki.api.bff.persistence.repositories.DepartmentStatisticsRepositoryInterface;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class DepartmentStatisticsPersistenceService implements DepartmentStatisticsPersistenceInterface {

    private final ModelMapper modelMapper;
    private DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface;
    private final SessionTenant sessionTenant;

    public DepartmentStatisticsPersistenceService(ModelMapper modelMapper, DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface, SessionTenant sessionTenant) {
        this.modelMapper = modelMapper;
        this.departmentStatisticsRepositoryInterface = departmentStatisticsRepositoryInterface;
        this.sessionTenant = sessionTenant;
    }

    @Override
    public List<DepartmentStatisticsDm> getPerDepartmentStatistics(Instant start, Instant end) {
        List<DepartmentStatisticsView> daos = departmentStatisticsRepositoryInterface.findAllDepartmentStatisticsInRange(
                start,
                end,
                sessionTenant.getTenantId()
        );

        List<DepartmentStatisticsDm> dms = modelMapper.map(
                daos,
                new TypeToken<List<DepartmentStatisticsResponseDto>>() {
                }.getType()
        );
        return dms;
    }
}
