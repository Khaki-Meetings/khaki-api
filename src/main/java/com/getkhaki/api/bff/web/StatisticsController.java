package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.CalendarEventService;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
