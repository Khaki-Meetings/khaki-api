package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.views.DepartmentStatisticsTmp;
import com.getkhaki.api.bff.persistence.models.views.DepartmentStatisticsView;
import com.getkhaki.api.bff.persistence.repositories.DepartmentStatisticsRepositoryInterface;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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


        List<DepartmentStatisticsTmp> daoTmpList = new ArrayList<DepartmentStatisticsTmp>();

        final int HOURS_PER_DAY = 8;
        final int SECONDS_PER_HOUR = 3600;

        for (DepartmentStatisticsView v : daoList) {
            DepartmentStatisticsTmp t = new DepartmentStatisticsTmp();
            t.departmentId = v.getDepartmentId();
            t.departmentName = v.getDepartmentName();
            t.totalSeconds = v.getTotalSeconds();
            t.inventorySecondsAvailable = Long.valueOf(v.getNumberEmployees() * SECONDS_PER_HOUR * HOURS_PER_DAY *
                    calculateNumberOfWorkdays(start, end));
            daoTmpList.add(t);
        }

        return modelMapper.map(
                daoTmpList,
                new TypeToken<List<DepartmentStatisticsResponseDto>>() {
                }.getType()
        );
    }

    public int calculateNumberOfWorkdays(Instant start, Instant end) {

        int numberWorkDays = 0;
        while (end.isAfter(start)) {
            end = end.minus(1, ChronoUnit.DAYS);
            DayOfWeek dayOfWeek = end.atOffset(ZoneOffset.UTC).getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
               numberWorkDays++;
            }
        }
        return numberWorkDays;

    }
}