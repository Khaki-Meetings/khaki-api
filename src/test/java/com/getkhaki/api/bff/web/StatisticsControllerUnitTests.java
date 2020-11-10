package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.services.CalendarEventService;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticsControllerUnitTests {
    private StatisticsController statisticsController;

    private StatisticsService statisticsService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        statisticsService = mock(StatisticsService.class);
        modelMapper = mock(ModelMapper.class);
        statisticsController = new StatisticsController(this.statisticsService, this.modelMapper);
    }

    @Test
    public void getOrganizersStatistics() {
    }
}
