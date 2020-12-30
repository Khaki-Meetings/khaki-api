package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.IntervalDe;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockRangeDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService;
    private final TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService;
    private final TimeBlockGeneratorFactory timeBlockGeneratorFactory;

    public StatisticsService(
            DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService,
            TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService,
            TimeBlockGeneratorFactory timeBlockGeneratorFactory) {
        this.departmentStatisticsPersistenceService = departmentStatisticsPersistenceService;
        this.timeBlockSummaryPersistenceService = timeBlockSummaryPersistenceService;
        this.timeBlockGeneratorFactory = timeBlockGeneratorFactory;
    }

    public List<TimeBlockSummaryDm> getTrailingStatistics(
            Instant start,
            IntervalDe interval,
            int count,
            StatisticsFilterDe filterDe
    ) {
        TimeBlockGeneratorInterface timeBlockGenerator = timeBlockGeneratorFactory.get(interval);
        List<TimeBlockRangeDm> timeBlockRangeList = timeBlockGenerator.generate(start, count);

        return timeBlockRangeList
                .stream()
                .map(
                        range -> timeBlockSummaryPersistenceService
                                .getTimeBlockSummary(range.getStart(), range.getEnd(), filterDe)
                )
                .collect(Collectors.toList());
    }
}
