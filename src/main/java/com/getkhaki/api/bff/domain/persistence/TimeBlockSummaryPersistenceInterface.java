package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;

import java.time.Instant;
import java.util.UUID;

public interface TimeBlockSummaryPersistenceInterface {
    TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe);
    TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe, UUID tenantId);
    TimeBlockSummaryDm getIndividualTimeBlockSummary(UUID employeeId, Instant start, Instant end, StatisticsFilterDe statsFilter);
}
