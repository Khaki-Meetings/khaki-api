package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventsEmployeeTimeDm;
import com.getkhaki.api.bff.domain.models.CalendarMeetingEfficacyDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;

import java.time.Instant;
import java.util.UUID;

public interface TimeBlockSummaryPersistenceInterface {
    TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe);
    TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe, UUID tenantId);

    TimeBlockSummaryDm getDepartmentTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe, String department);
    TimeBlockSummaryDm getDepartmentTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe, String department, UUID tenantId);

    TimeBlockSummaryDm getIndividualTimeBlockSummary(UUID employeeId, Instant start, Instant end, StatisticsFilterDe statsFilter);
    TimeBlockSummaryDm upsert(TimeBlockSummaryDm timeBlockSummary);

    CalendarEventsEmployeeTimeDm getCalendarEventEmployeeTime(Instant sDate, Instant eDate);
    CalendarEventsEmployeeTimeDm getCalendarEventEmployeeTime(Instant sDate, Instant eDate, String department);

    CalendarMeetingEfficacyDm getMeetingEfficacyAverages(Instant sDate, Instant eDate, StatisticsFilterDe filterDe);
    CalendarMeetingEfficacyDm getDepartmentMeetingEfficacyAverages(Instant sDate, Instant eDate, StatisticsFilterDe filterDe, String department);
}
