package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class StatisticsService {

    private DepartmentStatisticsPersistenceService departmentStatisticsPersistenceService;
    private OrganizersStatisticsPersistenceService organizersStatisticsPersistenceService;
    private  TimeBlockSummaryPersistenceService timeBlockSummaryPersistenceService;

    public StatisticsService(DepartmentStatisticsPersistenceService departmentStatisticsPersistenceService,OrganizersStatisticsPersistenceService organizersStatisticsPersistenceService,TimeBlockSummaryPersistenceService timeBlockSummaryPersistenceService) {
        this.departmentStatisticsPersistenceService=departmentStatisticsPersistenceService;
        this.organizersStatisticsPersistenceService=organizersStatisticsPersistenceService;
        this.timeBlockSummaryPersistenceService=timeBlockSummaryPersistenceService;
    }

    public List<DepartmentStatisticsDm> getPerDepartmentStatistics(ZonedDateTime start, ZonedDateTime end) {
        return this.departmentStatisticsPersistenceService.getPerDepartmentStatistics(start,end);

    }

    public OrganizersStatisticsDm getOrganizerStatistics(String email) {
        return this.organizersStatisticsPersistenceService.getOrganizerStatistics(email);
    }

    public TimeBlockSummaryDm getTimeBlockSummary(ZonedDateTime start, ZonedDateTime end) {
        return this.timeBlockSummaryPersistenceService.getTimeBlockSummary(start,end);
    }

    public List<TimeBlockSummaryDm> getTrailingStatistics(ZonedDateTime start, ZonedDateTime end, IntervalEnumDao interval) {
        return this.timeBlockSummaryPersistenceService.getTrailingStatistics(start,end,interval);
    }


}
