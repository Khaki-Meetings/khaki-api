package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
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
    private final DepartmentStatisticsRepositoryInterface departmentStatisticsRepository;
    private final SessionTenant sessionTenant;

    public DepartmentStatisticsPersistenceService(ModelMapper modelMapper, DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface, SessionTenant sessionTenant) {
        this.modelMapper = modelMapper;
        this.departmentStatisticsRepository = departmentStatisticsRepositoryInterface;
        this.sessionTenant = sessionTenant;
    }

    @Override
    public List<DepartmentStatisticsDm> getPerDepartmentStatistics(
            Instant start,
            Instant end,
            StatisticsFilterDe filterDe
    ) {
        List<DepartmentStatisticsView> daoList;
        switch (filterDe) {
            case External:
                daoList = departmentStatisticsRepository.findExternalDepartmentStatisticsInRange(
                        start,
                        end,
                        sessionTenant.getTenantId()
                );
                break;
            case Internal:
                daoList = departmentStatisticsRepository.findInternalDepartmentStatisticsInRange(
                        start,
                        end,
                        sessionTenant.getTenantId()
                );
                break;
            default:
                throw new RuntimeException("invalid filter" + filterDe);
        }

        return modelMapper.map(
                daoList,
                new TypeToken<List<DepartmentStatisticsResponseDto>>() {
                }.getType()
        );
    }
}
