package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.IntervalEnumDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.web.models.IntervalEnumDto;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class StatisticsService {

    private DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService;
    private OrganizersStatisticsPersistenceInterface organizersStatisticsPersistenceService;
    private TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService;

    public StatisticsService(
            DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService,
            OrganizersStatisticsPersistenceInterface organizersStatisticsPersistenceService,
            TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService
    ) {
        this.departmentStatisticsPersistenceService = departmentStatisticsPersistenceService;
        this.organizersStatisticsPersistenceService = organizersStatisticsPersistenceService;
        this.timeBlockSummaryPersistenceService = timeBlockSummaryPersistenceService;
    }

    public List<DepartmentStatisticsDm> getPerDepartmentStatistics(ZonedDateTime start, ZonedDateTime end) {
        return this.departmentStatisticsPersistenceService.getPerDepartmentStatistics(start, end);

    }

    public List<OrganizerStatisticsDm> getOrganizersStatistics(ZonedDateTime start, ZonedDateTime end, int count) {
        return this.organizersStatisticsPersistenceService.getOrganizersStatistics(start, end, count);
    }

    public TimeBlockSummaryDm getTimeBlockSummary(ZonedDateTime start, ZonedDateTime end) {
        return this.timeBlockSummaryPersistenceService.getTimeBlockSummary(start, end);
    }

    public List<TimeBlockSummaryDm> getTrailingStatistics(ZonedDateTime start, ZonedDateTime end, IntervalEnumDm interval) {
        return this.timeBlockSummaryPersistenceService.getTrailingStatistics(start, end, interval);
    }


}
