package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.IntervalDe;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockRangeDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;


@CommonsLog
@Service
public class StatisticsService {
    private final TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService;
    private final TimeBlockGeneratorFactory timeBlockGeneratorFactory;
    private final SessionTenant sessionTenant;

    public StatisticsService(
            TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService,
            TimeBlockGeneratorFactory timeBlockGeneratorFactory,
            SessionTenant sessionTenant
    ) {
        this.timeBlockSummaryPersistenceService = timeBlockSummaryPersistenceService;
        this.timeBlockGeneratorFactory = timeBlockGeneratorFactory;
        this.sessionTenant = sessionTenant;
    }

    public List<TimeBlockSummaryDm> getTrailingStatistics(
            Instant start,
            IntervalDe interval,
            int count,
            StatisticsFilterDe filterDe
    ) {
        TimeBlockGeneratorInterface timeBlockGenerator = timeBlockGeneratorFactory.get(interval);
        List<TimeBlockRangeDm> timeBlockRangeList = timeBlockGenerator.generate(start, count);

        UUID tenantId = sessionTenant.getTenantId();
        var futures = timeBlockRangeList
                .stream()
                .map(
                        range -> supplyAsync(() -> timeBlockSummaryPersistenceService
                                .getTimeBlockSummary(
                                        range.getStart(),
                                        range.getEnd(),
                                        filterDe,
                                        tenantId
                                ))
                )
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }
}
