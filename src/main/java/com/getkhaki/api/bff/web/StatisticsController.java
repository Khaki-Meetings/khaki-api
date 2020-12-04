package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.IntervalDe;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.DepartmentsStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.TimeBlockSummaryResponseDto;
import com.getkhaki.api.bff.web.models.TrailingStatisticsResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

@RequestMapping("/statistics")
@RestController
@CrossOrigin(origins = "*")
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ModelMapper modelMapper;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, ModelMapper modelMapper) {
        this.statisticsService = statisticsService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/bla")
    public String getBla() {
        return "bla";
    }

    @GetMapping("/organizers/{start}/{end}")
    public OrganizersStatisticsResponseDto getOrganizersStatistics(
            @PathVariable Instant start,
            @PathVariable Instant end,
            @RequestParam(required = false) OptionalInt optionalCount
    ) {
        List<OrganizerStatisticsDm> organizerStatisticsDmList = statisticsService
                .getOrganizersStatistics(start, end, optionalCount);
        OrganizersStatisticsResponseDto ret = new OrganizersStatisticsResponseDto();
        ret.setOrganizersStatistics(
                modelMapper.map(
                        organizerStatisticsDmList,
                        new TypeToken<List<OrganizerStatisticsResponseDto>>() {
                        }.getType()
                )
        );
        ret.setPage(1);
        ret.setCount(optionalCount);
        return ret;
    }


    @GetMapping("/summary/{start}/{end}")
    public TimeBlockSummaryResponseDto getTimeBlockSummary(
            @PathVariable Instant start, @PathVariable Instant end
    ) {
        return modelMapper.map(statisticsService.getTimeBlockSummary(start, end), TimeBlockSummaryResponseDto.class);
    }

    @GetMapping("/department/{start}/{end}")
    public DepartmentsStatisticsResponseDto getPerDepartmentStatistics(
            @PathVariable Instant start,
            @PathVariable Instant end
    ) {
        DepartmentsStatisticsResponseDto ret = new DepartmentsStatisticsResponseDto();
        ret.setDepartmentsStatistics(
                modelMapper.map(
                        statisticsService.getPerDepartmentStatistics(start, end),
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
            @PathVariable int count
    ) {
        TrailingStatisticsResponseDto ret = new TrailingStatisticsResponseDto();
        ret.setTimeBlockSummaries(statisticsService
                .getTrailingStatistics(start, interval, count)
                .stream()
                .map(
                        stat -> modelMapper.map(stat, TimeBlockSummaryResponseDto.class)
                )
                .collect(Collectors.toList())
        );

        return ret;
    }


}
