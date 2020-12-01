package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.IntervalEnumDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;

import java.time.Instant;
import java.util.List;

public interface TimeBlockSummaryPersistenceInterface {
    TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end);

    List<TimeBlockSummaryDm> getTrailingStatistics(Instant start, Instant end, IntervalEnumDm interval);

}
