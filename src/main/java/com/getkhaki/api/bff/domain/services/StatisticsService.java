package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.IntervalEnumDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;

@Service
public class StatisticsService {

    private final DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService;
    private final OrganizersStatisticsPersistenceInterface organizersStatisticsPersistenceService;
    private final TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService;

    public StatisticsService(
            DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService,
            OrganizersStatisticsPersistenceInterface organizersStatisticsPersistenceService,
            TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService
    ) {
        this.departmentStatisticsPersistenceService = departmentStatisticsPersistenceService;
        this.organizersStatisticsPersistenceService = organizersStatisticsPersistenceService;
        this.timeBlockSummaryPersistenceService = timeBlockSummaryPersistenceService;
    }

    public List<DepartmentStatisticsDm> getPerDepartmentStatistics(Instant start, Instant end) {
        return this.departmentStatisticsPersistenceService.getPerDepartmentStatistics(start, end);
    }

    public List<OrganizerStatisticsDm> getOrganizersStatistics(Instant start, Instant end, OptionalInt count) {
        return this.organizersStatisticsPersistenceService.getOrganizersStatistics(start, end, count);
    }

    public TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end) {
        return this.timeBlockSummaryPersistenceService.getTimeBlockSummary(start, end);
    }

    public List<TimeBlockSummaryDm> getTrailingStatistics(Instant start, IntervalEnumDm interval, int count) {
        Instant end = Instant.parse("2020-11-01T00:00:00.000Z");
        return List.of(this.timeBlockSummaryPersistenceService.getTimeBlockSummary(start, end));
    }


}
