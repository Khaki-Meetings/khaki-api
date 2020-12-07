package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;

import java.time.Instant;

public interface TimeBlockSummaryPersistenceInterface {
    TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end);
}
