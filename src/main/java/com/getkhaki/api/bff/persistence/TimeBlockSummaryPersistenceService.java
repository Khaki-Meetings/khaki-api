package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.CalendarEventsEmployeeTimeDm;
import com.getkhaki.api.bff.domain.models.GoalDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.domain.services.GoalService;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsEmployeeTimeView;
import com.getkhaki.api.bff.persistence.models.views.TimeBlockSummaryView;
import com.getkhaki.api.bff.persistence.repositories.GoalRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import com.getkhaki.api.bff.web.models.GoalMeasureDte;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

@Service
public class TimeBlockSummaryPersistenceService implements TimeBlockSummaryPersistenceInterface {
    private final ModelMapper modelMapper;
    private final TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface;
    private final SessionTenant sessionTenant;
    private final GoalService goalService;

    public TimeBlockSummaryPersistenceService(
            ModelMapper modelMapper,
            TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface,
            SessionTenant sessionTenant,
            GoalService goalService) {
        this.modelMapper = modelMapper;
        this.timeBlockSummaryRepositoryInterface = timeBlockSummaryRepositoryInterface;
        this.sessionTenant = sessionTenant;
        this.goalService = goalService;
    }

    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe) {
        return getTimeBlockSummary(start, end, filterDe, sessionTenant.getTenantId());

    }

    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe, UUID tenantId) {
        TimeBlockSummaryView timeBlockSummaryView;

        switch (filterDe) {
            case External:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findExternalTimeBlockSummaryInRange(
                        start, end, tenantId
                );

                break;
            case Internal:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findInternalTimeBlockSummaryInRange(
                        start, end, tenantId
                );

                break;
            default:
                throw new RuntimeException("invalid filter: " + filterDe);
        }

        val timeBlockSummaryDm = modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class);
        timeBlockSummaryDm.setEnd(end);
        timeBlockSummaryDm.setStart(start);
        Integer numWorkdays = timeBlockSummaryRepositoryInterface.findNumberOfWorkdaysBetweenDates(start, end);
        timeBlockSummaryDm.setNumWorkdays(numWorkdays);

        return timeBlockSummaryDm;
    }

    @Override
    public TimeBlockSummaryDm getIndividualTimeBlockSummary(
            UUID employeeId, Instant start, Instant end, StatisticsFilterDe filterDe
    ) {
        TimeBlockSummaryView timeBlockSummaryView;

        switch (filterDe) {
            case External:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findIndividualExternalTimeBlockSummaryInRange(
                        employeeId, start, end, sessionTenant.getTenantId()
                );

                break;
            case Internal:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findIndividualInternalTimeBlockSummaryInRange(
                        employeeId, start, end, sessionTenant.getTenantId()
                );

                break;
            default:
                throw new RuntimeException("invalid filter: " + filterDe);
        }

        if (timeBlockSummaryView == null) {
            return new TimeBlockSummaryDm().setMeetingCount(0).setTotalSeconds(0L);
        }

        val timeBlockSummaryDm = modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class);
        timeBlockSummaryDm.setEnd(end);
        timeBlockSummaryDm.setStart(start);

        return timeBlockSummaryDm;
    }

    @Override
    public CalendarEventsEmployeeTimeDm getCalendarEventEmployeeTime(Instant sDate, Instant eDate) {

        GoalDm systemGoalDm = new GoalDm();
        systemGoalDm.setGreaterThanOrEqualTo(40);

        GoalDm goalDm = goalService.getGoals().stream().filter(g -> GoalMeasureDte.MeetingPercentageThreshold.equals(g.getMeasure()))
            .findAny()
            .orElse(systemGoalDm);

        CalendarEventsEmployeeTimeView calendarEventsEmployeeTimeView = timeBlockSummaryRepositoryInterface
                .getCalendarEventEmployeeTime(sessionTenant.getTenantId(), sDate, eDate, goalDm.getGreaterThanOrEqualTo());

        return modelMapper.map(calendarEventsEmployeeTimeView, CalendarEventsEmployeeTimeDm.class);
    }
}
