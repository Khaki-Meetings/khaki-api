package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.IntervalDe;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.DepartmentsStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.StatisticsFilterDte;
import com.getkhaki.api.bff.web.models.TimeBlockSummaryResponseDto;
import com.getkhaki.api.bff.web.models.TrailingStatisticsResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/statistics")
@RestController
@CrossOrigin(origins = "*")
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
                .getOrganizersStatistics(
                        start,
                        end,
                        pageable,
                        modelMapper.map(
                                filter.orElse(StatisticsFilterDte.External),
                                StatisticsFilterDe.class
                        )
                );
        return organizerStatisticsDmList.map(dm -> modelMapper.map(dm, OrganizerStatisticsResponseDto.class));
    }


    @GetMapping("/summary/{start}/{end}")
    public TimeBlockSummaryResponseDto getTimeBlockSummary(
            @PathVariable Instant start,
            @PathVariable Instant end,
            @RequestParam(required = false) Optional<StatisticsFilterDte> filter
    ) {
        var filterDe = modelMapper.map(
                filter.orElse(StatisticsFilterDte.External),
                StatisticsFilterDe.class
        );
        return modelMapper.map(
                timeBlockSummaryPersistenceService.getTimeBlockSummary(start, end, filterDe),
                TimeBlockSummaryResponseDto.class
        );
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
                        departmentStatisticsPersistenceService.getPerDepartmentStatistics(
                                start,
                                end,
                                modelMapper.map(filter.orElse(StatisticsFilterDte.External), StatisticsFilterDe.class)
                        ),
                        new TypeToken<List<DepartmentStatisticsResponseDto>>() {
                        }.getType()
                )
        );

        return ret;
    }

    @GetMapping("/trailing/{start}/{interval}/{count}")
    public TrailingStatisticsResponseDto getTrailingStatistics(
            @PathVariable Instant start,
            @PathVariable IntervalDe interval,
            @PathVariable int count,
            @RequestParam(required = false) Optional<StatisticsFilterDte> filter
    ) {
        TrailingStatisticsResponseDto ret = new TrailingStatisticsResponseDto();
        ret.setTimeBlockSummaries(statisticsService
                .getTrailingStatistics(start, interval, count, modelMapper.map(
                        filter.orElse(StatisticsFilterDte.External),
                        StatisticsFilterDe.class
                        )
                )
                .stream()
                .map(
                        stat -> modelMapper.map(stat, TimeBlockSummaryResponseDto.class)
                )
                .collect(Collectors.toList())
        );

        return ret;
    }


}
