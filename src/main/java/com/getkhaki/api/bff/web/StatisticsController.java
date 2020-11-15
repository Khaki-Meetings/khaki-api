package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.persistence.models.DepartmentStatisticsDao;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.TimeBlockSummaryResponseDto;
import com.getkhaki.api.bff.web.models.TrailingStatisticsResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable ZonedDateTime start,
            @PathVariable ZonedDateTime end,
            @RequestParam OptionalInt optionalCount
    ) {
        int count = optionalCount.orElse(5);
        List<OrganizerStatisticsDm> organizerStatisticsDmList = statisticsService
                .getOrganizersStatistics(start, end, count);
        OrganizersStatisticsResponseDto ret = new OrganizersStatisticsResponseDto();
        ret.setOrganizersStatistics(
                modelMapper.map(
                        organizerStatisticsDmList,
                        new TypeToken<List<OrganizerStatisticsResponseDto>>() {
                        }.getType()
                )
        );
        ret.setPage(1);
        ret.setCount(count);
        return ret;
    }


    @GetMapping("/summary")
    public TimeBlockSummaryResponseDto getTimeBlockSummary(@PathVariable(name = "start") ZonedDateTime start, @PathVariable(name = "end") ZonedDateTime end) {
        return modelMapper.map(statisticsService.getTimeBlockSummary(start, end), TimeBlockSummaryResponseDto.class);
    }

    @GetMapping("/department")
    public List<DepartmentStatisticsDao> getPerDepartmentStatistics(@PathVariable(name = "start") ZonedDateTime start, @PathVariable(name = "end") ZonedDateTime end) {

        return modelMapper.map(statisticsService.getPerDepartmentStatistics(start, end), new TypeToken<List<DepartmentStatisticsDao>>() {
        }.getType());
    }

    @GetMapping("/trailing")
    public TrailingStatisticsResponseDto getTrailingStatistics(@PathVariable(name = "start") ZonedDateTime start, @PathVariable(name = "end") ZonedDateTime end, @PathVariable(name = "interval") IntervalEnumDao interval) {

        return modelMapper.map(statisticsService.getTrailingStatistics(start, end, interval), TrailingStatisticsResponseDto.class);
    }


}
