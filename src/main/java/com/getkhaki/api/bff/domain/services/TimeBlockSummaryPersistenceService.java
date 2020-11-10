package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDto;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TimeBlockSummaryPersistenceService implements TimeBlockSummaryPersistenceInterface {
    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(ZonedDateTime start, ZonedDateTime end) {
        return null;
    }

    @Override
    public List<TimeBlockSummaryDm> getTrailingStatistics(ZonedDateTime start, ZonedDateTime end, IntervalEnumDto interval) {
        return null;
    }
}
