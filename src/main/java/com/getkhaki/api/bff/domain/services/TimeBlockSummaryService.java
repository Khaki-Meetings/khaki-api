package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TimeBlockSummaryService {
    private final TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceInterface;

    @Autowired
    public TimeBlockSummaryService(TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceInterface) {
        this.timeBlockSummaryPersistenceInterface = timeBlockSummaryPersistenceInterface;
    }

    public TimeBlockSummaryDm updateTimeBlockSummary(TimeBlockSummaryDm timeBlockSummaryDm) {
        return this.timeBlockSummaryPersistenceInterface.upsert(timeBlockSummaryDm);
    }

    public TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe,
                                                  UUID organizationId) {
        return timeBlockSummaryPersistenceInterface.getTimeBlockSummary(
            start, end, filterDe, organizationId);
    }
}