package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.*;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.GoalPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.web.models.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/statistics")
public class StatisticsController {
    private final StatisticsService statisticsService;
    private final OrganizersStatisticsPersistenceInterface organizersStatisticsPersistenceService;
    private final TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService;
    private final DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService;
    private final ModelMapper modelMapper;

    public StatisticsController(
            StatisticsService statisticsService,
            OrganizersStatisticsPersistenceInterface organizersStatisticsPersistenceService,
            TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService,
            DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService,
            ModelMapper modelMapper
    ) {
        this.statisticsService = statisticsService;
        this.organizersStatisticsPersistenceService = organizersStatisticsPersistenceService;
        this.timeBlockSummaryPersistenceService = timeBlockSummaryPersistenceService;
        this.departmentStatisticsPersistenceService = departmentStatisticsPersistenceService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/organizers/{start}/{end}")
    public Page<OrganizerStatisticsResponseDto> getOrganizersStatistics(
            @PathVariable Instant start,
            @PathVariable Instant end,
            @RequestParam(required = false) Optional<StatisticsFilterDte> filter,
            Pageable pageable
    ) {
        Page<OrganizerStatisticsDm> organizerStatisticsDmList = organizersStatisticsPersistenceService
                .getOrganizersStatistics(start, end, pageable, modelMapper.map(
                        filter.orElse(StatisticsFilterDte.External), StatisticsFilterDe.class)
                );

        return organizerStatisticsDmList.map(dm -> modelMapper.map(dm, OrganizerStatisticsResponseDto.class));
    }

    @GetMapping("/organizers/aggregate/{start}/{end}")
    public Page<OrganizerStatisticsAggregateResponseDto> getAggregateOrganizersStatistics(
            @PathVariable Instant start,
            @PathVariable Instant end,
            Pageable pageable,
            @RequestParam(required = false) Optional<String> department
    ) {
        String departmentName = department.orElse("");

        Page<OrganizerStatisticsAggregateDm> organizerStatisticsDmList = organizersStatisticsPersistenceService
                .getAggregateOrganizersStatistics(start, end, departmentName, pageable);

        return organizerStatisticsDmList.map(dm -> modelMapper.map(dm, OrganizerStatisticsAggregateResponseDto.class));
    }

    @GetMapping("/summary/{start}/{end}")
    public TimeBlockSummaryResponseDto getTimeBlockSummary(
            @PathVariable Instant start,
            @PathVariable Instant end,
            @RequestParam(required = false) Optional<StatisticsFilterDte> filter,
            @RequestParam(required = false) Optional<String> department
    ) {
        StatisticsFilterDe filterDe = modelMapper.map(
                filter.orElse(StatisticsFilterDte.External),
                StatisticsFilterDe.class
        );

        String departmentName = department.orElse("");

        if (!departmentName.isEmpty()) {

            TimeBlockSummaryDm timeBlockSummaryDm = timeBlockSummaryPersistenceService.getDepartmentTimeBlockSummary(start, end, filterDe, departmentName);

            CalendarMeetingEfficacyDm calendarMeetingEfficacyDm = timeBlockSummaryPersistenceService.getDepartmentMeetingEfficacyAverages(start, end, filterDe, departmentName);
            if (calendarMeetingEfficacyDm != null)  {
                timeBlockSummaryDm.setAverageMeetingLength(calendarMeetingEfficacyDm.getAverageMeetingLength());
                timeBlockSummaryDm.setAverageStaffTimePerMeeting(calendarMeetingEfficacyDm.getAverageStaffTimePerMeeting());
            }

            CalendarEventsEmployeeTimeDm cal = timeBlockSummaryPersistenceService.getCalendarEventEmployeeTime(start, end, departmentName);
            if (cal != null) {
                timeBlockSummaryDm.setNumEmployeesOverTimeThreshold(cal.getNumOverThreshold());
            }

            return modelMapper.map(timeBlockSummaryDm, TimeBlockSummaryResponseDto.class);
        }

        TimeBlockSummaryDm timeBlockSummaryDm = timeBlockSummaryPersistenceService.getTimeBlockSummary(start, end, filterDe);

        CalendarMeetingEfficacyDm calendarMeetingEfficacyDm = timeBlockSummaryPersistenceService.getMeetingEfficacyAverages(start, end, filterDe);
        if (calendarMeetingEfficacyDm != null)  {
            timeBlockSummaryDm.setAverageMeetingLength(calendarMeetingEfficacyDm.getAverageMeetingLength());
            timeBlockSummaryDm.setAverageStaffTimePerMeeting(calendarMeetingEfficacyDm.getAverageStaffTimePerMeeting());
        }

        CalendarEventsEmployeeTimeDm cal = timeBlockSummaryPersistenceService.getCalendarEventEmployeeTime(start, end);
        if (cal != null) {
            timeBlockSummaryDm.setNumEmployeesOverTimeThreshold(cal.getNumOverThreshold());
        }

        return modelMapper.map(timeBlockSummaryDm, TimeBlockSummaryResponseDto.class);
    }

    @GetMapping("/department/{start}/{end}")
    public DepartmentsStatisticsResponseDto getPerDepartmentStatistics(
            @PathVariable Instant start,
            @PathVariable Instant end,
            @RequestParam(required = false) Optional<StatisticsFilterDte> filter
    ) {
        DepartmentsStatisticsResponseDto ret = new DepartmentsStatisticsResponseDto();

        ret.setDepartmentsStatistics(
                modelMapper.map(
                        departmentStatisticsPersistenceService.getPerDepartmentStatistics(start, end, modelMapper.map(
                                filter.orElse(StatisticsFilterDte.External), StatisticsFilterDe.class)
                        ),
                        new TypeToken<List<DepartmentStatisticsResponseDto>>() {}.getType()
                )
        );

        return ret;
    }

    @GetMapping("/trailing/{start}/{interval}/{count}")
    public TrailingStatisticsResponseDto getTrailingStatistics(
            @PathVariable Instant start,
            @PathVariable IntervalDe interval,
            @PathVariable int count,
            @RequestParam(required = false) Optional<StatisticsFilterDte> filter,
            @RequestParam(required = false) Optional<String> department
    ) {
        TrailingStatisticsResponseDto ret = new TrailingStatisticsResponseDto();

        String departmentName = department.orElse("");

        if (!departmentName.isEmpty()) {

            ret.setTimeBlockSummaries(
                    statisticsService.getDepartmentTrailingStatistics(start, interval, count, modelMapper.map(
                            filter.orElse(StatisticsFilterDte.External), StatisticsFilterDe.class), departmentName
                    )
                            .stream()
                            .map(stat -> modelMapper.map(stat, TimeBlockSummaryResponseDto.class))
                            .collect(Collectors.toList())
            );
            return ret;
        }

        ret.setTimeBlockSummaries(
                statisticsService.getTrailingStatistics(start, interval, count, modelMapper.map(
                        filter.orElse(StatisticsFilterDte.External), StatisticsFilterDe.class)
                )
                .stream()
                .map(stat -> modelMapper.map(stat, TimeBlockSummaryResponseDto.class))
                .collect(Collectors.toList())
        );

        return ret;
    }

    @GetMapping("/individual/{employeeId}/{start}/{end}")
    public TimeBlockSummaryResponseDto getIndividualStatistics(
            @PathVariable UUID employeeId,
            @PathVariable Instant start,
            @PathVariable Instant end,
            @RequestParam(required = false) Optional<StatisticsFilterDte> filter
    ) {
        StatisticsFilterDe filterDe = modelMapper.map(
                filter.orElse(StatisticsFilterDte.External),
                StatisticsFilterDe.class
        );

        return modelMapper.map(
                timeBlockSummaryPersistenceService.getIndividualTimeBlockSummary(employeeId, start, end, filterDe),
                TimeBlockSummaryResponseDto.class
        );
    }
}
