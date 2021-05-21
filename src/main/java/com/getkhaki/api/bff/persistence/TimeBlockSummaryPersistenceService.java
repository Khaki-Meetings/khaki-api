package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.*;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.domain.services.GoalService;
import com.getkhaki.api.bff.domain.services.KhakiModelMapper;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.models.TimeBlockSummaryDao;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsEmployeeTimeView;
import com.getkhaki.api.bff.persistence.models.views.CalendarMeetingEfficacyView;
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

        Optional<TimeBlockSummaryDao> timeBlockSummaryDao =
                timeBlockSummaryRepositoryInterface.findDistinctByOrganizationAndStartAndFilter(
                        tenantId, start, end, filterDe.toString());

        switch (filterDe) {
            case External:

                if (timeBlockSummaryDao.isPresent()) {
                    timeBlockSummaryDm = modelMapper.map(timeBlockSummaryDao.get(), TimeBlockSummaryDm.class);
                    log.info("Using serialized data for " + start +  " " + filterDe.toString());
                    break;
                }

                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findExternalTimeBlockSummaryInRange(
                        start, end, tenantId
                );

                // Needs a rework - an POC attempt...
                TimeBlockSummaryView extTimeBlockAggregateSummaryView = timeBlockSummaryRepositoryInterface.findExternalTimeBlockAggregateSummaryInRange(
                        start, end, tenantId
                );

                timeBlockSummaryDm = modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class);

                timeBlockSummaryDm.setMeetingLengthSeconds(extTimeBlockAggregateSummaryView.getMeetingLengthSeconds());
                timeBlockSummaryDm.setTotalInternalMeetingAttendees(extTimeBlockAggregateSummaryView.getTotalInternalMeetingAttendees());
                timeBlockSummaryDm.setTotalMeetingAttendees(extTimeBlockAggregateSummaryView.getTotalMeetingAttendees());

                log.info("Using live query for " + start +  " " + filterDe.toString());

                timeBlockSummaryDm.setOrganizationId(tenantId);
                timeBlockSummaryDm.setStart(start);
                timeBlockSummaryDm.setEnd(end);
                timeBlockSummaryDm.setFilterDe(filterDe);
                upsert(timeBlockSummaryDm);

                break;

            case Internal:

                if (timeBlockSummaryDao.isPresent()) {
                    timeBlockSummaryDm = modelMapper.map(timeBlockSummaryDao.get(), TimeBlockSummaryDm.class);
                    log.info("Using serialized data for " + start +  " " + filterDe.toString());
                    break;
                }

                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findInternalTimeBlockSummaryInRange(
                        start, end, tenantId
                );

                // Needs a rework - an POC attempt...
                TimeBlockSummaryView timeBlockAggregateSummaryView = timeBlockSummaryRepositoryInterface.findInternalTimeBlockAggregateSummaryInRange(
                        start, end, tenantId
                );

                timeBlockSummaryDm = modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class);

                timeBlockSummaryDm.setMeetingLengthSeconds(timeBlockAggregateSummaryView.getMeetingLengthSeconds());
                timeBlockSummaryDm.setTotalInternalMeetingAttendees(timeBlockAggregateSummaryView.getTotalInternalMeetingAttendees());
                timeBlockSummaryDm.setTotalMeetingAttendees(timeBlockAggregateSummaryView.getTotalMeetingAttendees());

                log.info("Using live query for " + start +  " " + filterDe.toString());

                timeBlockSummaryDm.setOrganizationId(tenantId);
                timeBlockSummaryDm.setStart(start);
                timeBlockSummaryDm.setEnd(end);
                timeBlockSummaryDm.setFilterDe(filterDe);
                upsert(timeBlockSummaryDm);

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
    public TimeBlockSummaryDm getDepartmentTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe, String department) {
        return getDepartmentTimeBlockSummary(start, end, filterDe, department, sessionTenant.getTenantId());
    }

    @Override
    public TimeBlockSummaryDm getDepartmentTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe, String department, UUID tenantId) {
        TimeBlockSummaryView timeBlockSummaryView;
        TimeBlockSummaryDm timeBlockSummaryDm;

        Optional<TimeBlockSummaryDao> timeBlockSummaryDao =
                timeBlockSummaryRepositoryInterface.findDistinctByOrganizationAndStartAndFilter(
                        tenantId, start, end, filterDe.toString());

        switch (filterDe) {
            case External:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findDepartmentExternalTimeBlockSummaryInRange(
                        start, end, department, tenantId
                );
                timeBlockSummaryDm = modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class);

                // Needs a rework - an POC attempt...
                TimeBlockSummaryView extTimeBlockAggregateSummaryView = timeBlockSummaryRepositoryInterface.
                findDepartmentExternalTimeBlockAggregateSummaryInRange(
                        start, end, department, tenantId
                );

                timeBlockSummaryDm.setMeetingLengthSeconds(extTimeBlockAggregateSummaryView.getMeetingLengthSeconds());
                timeBlockSummaryDm.setTotalInternalMeetingAttendees(extTimeBlockAggregateSummaryView.getTotalInternalMeetingAttendees());
                timeBlockSummaryDm.setTotalMeetingAttendees(extTimeBlockAggregateSummaryView.getTotalMeetingAttendees());

                log.info("Using live query for " + start +  " " + filterDe.toString());

                break;

            case Internal:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findDepartmentInternalTimeBlockSummaryInRange(
                        start, end, department, tenantId
                );
                timeBlockSummaryDm = modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class);

                // Needs a rework - an POC attempt...
                TimeBlockSummaryView timeBlockAggregateSummaryView = timeBlockSummaryRepositoryInterface.findDepartmentInternalTimeBlockAggregateSummaryInRange(
                        start, end, department, tenantId
                );

                timeBlockSummaryDm.setMeetingLengthSeconds(timeBlockAggregateSummaryView.getMeetingLengthSeconds());
                timeBlockSummaryDm.setTotalInternalMeetingAttendees(timeBlockAggregateSummaryView.getTotalInternalMeetingAttendees());
                timeBlockSummaryDm.setTotalMeetingAttendees(timeBlockAggregateSummaryView.getTotalMeetingAttendees());

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
    public CalendarEventsEmployeeTimeDm getCalendarEventEmployeeTime(Instant sDate, Instant eDate, String department) {

        GoalDm systemGoalDm = new GoalDm();
        systemGoalDm.setGreaterThanOrEqualTo(40);

        GoalDm goalDm = goalService.getGoals().stream().filter(g -> GoalMeasureDte.MeetingPercentageThreshold.equals(g.getMeasure()))
                .findAny()
                .orElse(systemGoalDm);

        CalendarEventsEmployeeTimeView calendarEventsEmployeeTimeView = timeBlockSummaryRepositoryInterface
                .getCalendarEventEmployeeTime(sessionTenant.getTenantId(), sDate, eDate, goalDm.getGreaterThanOrEqualTo(), department);

        return modelMapper.map(calendarEventsEmployeeTimeView, CalendarEventsEmployeeTimeDm.class);
    }

    @Override
    public CalendarMeetingEfficacyDm getMeetingEfficacyAverages(Instant sDate, Instant eDate, StatisticsFilterDe filterDe) {

        CalendarMeetingEfficacyView calendarMeetingEfficacyView;

        switch (filterDe) {
            case External:
                calendarMeetingEfficacyView = timeBlockSummaryRepositoryInterface.getExternalMeetingEfficacyAverages(
                        sessionTenant.getTenantId(), sDate, eDate
                );
                break;
            case Internal:
                calendarMeetingEfficacyView = timeBlockSummaryRepositoryInterface.getInternalMeetingEfficacyAverages(
                        sessionTenant.getTenantId(), sDate, eDate
                );
                break;
            default:
                throw new RuntimeException("invalid filter: " + filterDe);
        }

        return modelMapper.map(calendarMeetingEfficacyView, CalendarMeetingEfficacyDm.class);
    }

    @Override
    public CalendarMeetingEfficacyDm getDepartmentMeetingEfficacyAverages(Instant sDate,
                  Instant eDate, StatisticsFilterDe filterDe, String department) {

        CalendarMeetingEfficacyView calendarMeetingEfficacyView;

        switch (filterDe) {
            case External:
                calendarMeetingEfficacyView = timeBlockSummaryRepositoryInterface.getDepartmentExternalMeetingEfficacyAverages(
                        sessionTenant.getTenantId(), sDate, eDate, department
                );
                break;
            case Internal:
                calendarMeetingEfficacyView = timeBlockSummaryRepositoryInterface.getDepartmentInternalMeetingEfficacyAverages(
                        sessionTenant.getTenantId(), sDate, eDate, department
                );
                break;
            default:
                throw new RuntimeException("invalid filter: " + filterDe);
        }

        return modelMapper.map(calendarMeetingEfficacyView, CalendarMeetingEfficacyDm.class);
    }

    @Override
    public TimeBlockSummaryDm upsert(TimeBlockSummaryDm timeBlockSummary) {

        TimeBlockSummaryDao timeBlockSummaryDao = this.modelMapper.mapTimeBlockSummaryDmToDao(timeBlockSummary);

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
