package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;

import java.time.Instant;
import java.util.concurrent.Future;

public interface TimeBlockSummaryPersistenceInterface {
    Future<TimeBlockSummaryDm> getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe);
}
