package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.IntervalEnumDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.TimeBlockSummaryResponseDto;
import com.getkhaki.api.bff.web.models.TrailingStatisticsResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.OptionalInt;

@RequestMapping("/statistics")
@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final ModelMapper modelMapper;

    @Autowired
    public StatisticsController(StatisticsService statisticsService, ModelMapper modelMapper) {
        this.statisticsService = statisticsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/organizersStatistics/{start}/{end}")
    public OrganizersStatisticsResponseDto getOrganizersStatistics(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end,
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


    @GetMapping("/summary")
    public TimeBlockSummaryResponseDto getTimeBlockSummary(@PathVariable(name = "start") ZonedDateTime start, @PathVariable(name = "end") ZonedDateTime end) {
        return modelMapper.map(statisticsService.getTimeBlockSummary(start, end), TimeBlockSummaryResponseDto.class);
    }

    @GetMapping("/department")
    public List<DepartmentStatisticsResponseDto> getPerDepartmentStatistics(
            @PathVariable(name = "start") Instant start,
            @PathVariable(name = "end") Instant end
    ) {

//        return modelMapper.map(statisticsService.getPerDepartmentStatistics(start, end), new TypeToken<List<DepartmentStatisticsDao>>() {
//        }.getType());
        return null;
    }

    @GetMapping("/trailing/{start}/{end}")
    public TrailingStatisticsResponseDto getTrailingStatistics(
            @PathVariable(name = "start") ZonedDateTime start,
            @PathVariable(name = "end") ZonedDateTime end,
            @RequestParam(name = "interval") IntervalEnumDao interval
    ) {
        IntervalEnumDm intervalEnumDm = modelMapper.map(interval, IntervalEnumDm.class);
        return modelMapper.map(
                statisticsService.getTrailingStatistics(start, end, intervalEnumDm),
                TrailingStatisticsResponseDto.class
        );
    }


}
