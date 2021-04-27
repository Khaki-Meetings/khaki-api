package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.*;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.domain.services.GoalService;
import com.getkhaki.api.bff.domain.services.KhakiModelMapper;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.models.TimeBlockSummaryDao;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsEmployeeTimeView;
import com.getkhaki.api.bff.persistence.models.views.TimeBlockSummaryView;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import com.getkhaki.api.bff.web.models.GoalMeasureDte;
import lombok.extern.apachecommons.CommonsLog;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@CommonsLog
@Service
public class TimeBlockSummaryPersistenceService implements TimeBlockSummaryPersistenceInterface {
    private final KhakiModelMapper modelMapper;
    private final TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface;
    private final SessionTenant sessionTenant;
    private final GoalService goalService;
    private final OrganizationRepositoryInterface organizationRepositoryInterface;

    public TimeBlockSummaryPersistenceService(
            KhakiModelMapper modelMapper,
            TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface,
            SessionTenant sessionTenant,
            GoalService goalService, OrganizationRepositoryInterface organizationRepositoryInterface) {
        this.modelMapper = modelMapper;
        this.timeBlockSummaryRepositoryInterface = timeBlockSummaryRepositoryInterface;
        this.sessionTenant = sessionTenant;
        this.goalService = goalService;
        this.organizationRepositoryInterface = organizationRepositoryInterface;
    }

    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe) {
        return getTimeBlockSummary(start, end, filterDe, sessionTenant.getTenantId());

    }

    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe, UUID tenantId) {
        TimeBlockSummaryView timeBlockSummaryView;
        TimeBlockSummaryDm timeBlockSummaryDm;

        switch (filterDe) {
            case External:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findExternalTimeBlockSummaryInRange(
                        start, end, tenantId
                );
                timeBlockSummaryDm = modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class);
                break;
            case Internal:
                Optional<TimeBlockSummaryDao> timeBlockSummaryDao =
                        timeBlockSummaryRepositoryInterface.findDistinctByOrganizationAndStartAndFilter(
                                tenantId, start, end, filterDe.toString());

                if (timeBlockSummaryDao.isPresent()) {
                    timeBlockSummaryDm = modelMapper.map(timeBlockSummaryDao.get(), TimeBlockSummaryDm.class);
                    log.info("Using serialized data for " + start +  " " + filterDe.toString());
                    break;
                }
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findInternalTimeBlockSummaryInRange(
                        start, end, tenantId
                );
                timeBlockSummaryDm = modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class);
                log.info("Using live query for " + start +  " " + filterDe.toString());
                break;
            default:
                throw new RuntimeException("invalid filter: " + filterDe);
        }

        timeBlockSummaryDm.setEnd(end);
        timeBlockSummaryDm.setStart(start);
        Integer numWorkdays = timeBlockSummaryRepositoryInterface.findNumberOfWorkdaysBetweenDates(start, end);
        timeBlockSummaryDm.setNumWorkdays(numWorkdays);
        timeBlockSummaryDm.setOrganizationId(tenantId);
        timeBlockSummaryDm.setFilterDe(filterDe);
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

    @Override
    public TimeBlockSummaryDm upsert(TimeBlockSummaryDm timeBlockSummary) {

        TimeBlockSummaryDao timeBlockSummaryDao = this.modelMapper.map(timeBlockSummary, TimeBlockSummaryDao.class);

        OrganizationDao organizationDao = organizationRepositoryInterface
                .findById(timeBlockSummary.getOrganizationId()).orElseThrow(() -> new RuntimeException("Organization required"));

        log.info("Searching for timeblock: " + organizationDao.getName() + " " + organizationDao.getId() + " " +
                timeBlockSummary.getStart() + " " + timeBlockSummary.getFilterDe().toString());

        val timeBlockSummaryDaoOp = timeBlockSummaryRepositoryInterface
                .findDistinctByOrganizationAndStartAndFilter(organizationDao.getId(),
                        timeBlockSummary.getStart(), timeBlockSummary.getEnd(),
                        timeBlockSummary.getFilterDe().toString());

        timeBlockSummaryDaoOp.ifPresentOrElse(
                dao -> {
                    timeBlockSummaryDao.setId(timeBlockSummaryDaoOp.get().getId());
                },
                () -> {
                    timeBlockSummaryDao.setOrganizationId(organizationDao.getId());
                })
        ;
        timeBlockSummaryRepositoryInterface.save(timeBlockSummaryDao);


        return this.modelMapper.map(timeBlockSummaryDao, TimeBlockSummaryDm.class);
    }
}
