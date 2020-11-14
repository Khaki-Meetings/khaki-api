package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.persistence.models.DepartmentStatisticsDao;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;
import com.getkhaki.api.bff.persistence.models.TrailingStatisticsDao;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.TimeBlockSummaryResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

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

    @GetMapping("/organizer")
    public OrganizersStatisticsResponseDto getOrganizersStatistics(
            @PathVariable(name = "start") ZonedDateTime start,
            @PathVariable(name = "end") ZonedDateTime end
    ) {
        return modelMapper.map(
                statisticsService.getOrganizerStatistics("connected-user-mail"),
                OrganizersStatisticsResponseDto.class
        );
    }


    @GetMapping("/summary")
    public TimeBlockSummaryResponseDto getTimeBlockSummary(@PathVariable(name = "start") ZonedDateTime start, @PathVariable(name = "end") ZonedDateTime end) {
        return modelMapper.map(statisticsService.getTimeBlockSummary(start, end), TimeBlockSummaryResponseDto.class);
    }

    @GetMapping("/department")
    public List<DepartmentStatisticsDao> getPerDepartmentStatistics(@PathVariable(name = "start") ZonedDateTime start, @PathVariable(name = "end") ZonedDateTime end) {

        return modelMapper.map(statisticsService.getPerDepartmentStatistics(start, end), new TypeToken<List<DepartmentStatisticsDao>>() {}.getType());
    }

    @GetMapping("/trailing")
    public TrailingStatisticsDao getTrailingStatistics(@PathVariable(name = "start") ZonedDateTime start, @PathVariable(name = "end") ZonedDateTime end, @PathVariable(name = "interval") IntervalEnumDao interval) {

        return modelMapper.map(statisticsService.getTrailingStatistics(start, end, interval), TrailingStatisticsDao.class);
    }


}
