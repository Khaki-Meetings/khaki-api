package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.persistence.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class StatisticsControllerUnitTests {
    private StatisticsController statisticsController;

    private StatisticsService underTest;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        underTest = mock(StatisticsService.class);
        modelMapper = mock(ModelMapper.class);
        statisticsController = new StatisticsController(this.underTest, this.modelMapper);
    }

    @Test
    public void getOrganizersStatistics() {


        ZonedDateTime startTest = ZonedDateTime.parse("2019-03-27T10:15:30");
        ZonedDateTime endTest = ZonedDateTime.now();

        OrganizersStatisticsDao organizersStatisticsResponseDto = statisticsController.getOrganizersStatistics(startTest,endTest);
        assertThat(organizersStatisticsResponseDto).isNotNull();



    }


    @Test
    public void getTimeBlockSummary() {


        ZonedDateTime startTest = ZonedDateTime.parse("2019-03-27T10:15:30");
        ZonedDateTime endTest = ZonedDateTime.now();

        TimeBlockSummaryDao timeBlockSummaryResponseDto = statisticsController.getTimeBlockSummary(startTest,endTest);
        assertThat(timeBlockSummaryResponseDto).isNotNull();


    }


    @Test
    public void getPerDepartmentStatistics() {

        ZonedDateTime startTest = ZonedDateTime.parse("2019-03-27T10:15:30");
        ZonedDateTime endTest = ZonedDateTime.now();


        List<DepartmentStatisticsDao> departmentStatisticsResponseDtoList = statisticsController.getPerDepartmentStatistics(startTest,endTest);
        assertThat(departmentStatisticsResponseDtoList).isNotNull();

    }


    @Test
    public void getTrailingStatistics() {

        ZonedDateTime startTest = ZonedDateTime.parse("2019-03-27T10:15:30");
        ZonedDateTime endTest = ZonedDateTime.now();



        TrailingStatisticsDao trailingStatisticsResponseDto = statisticsController.getTrailingStatistics(startTest,endTest, IntervalEnumDao.Interval1);
        assertThat(trailingStatisticsResponseDto).isNotNull();


    }
}
