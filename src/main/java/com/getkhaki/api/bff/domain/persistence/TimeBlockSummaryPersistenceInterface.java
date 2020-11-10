package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDto;

import java.time.ZonedDateTime;
import java.util.List;

public interface TimeBlockSummaryPersistenceInterface {
    TimeBlockSummaryDm getTimeBlockSummary(ZonedDateTime start, ZonedDateTime end);
    List<TimeBlockSummaryDm> getTrailingStatistics(ZonedDateTime start, ZonedDateTime end, IntervalEnumDto interval);

}
