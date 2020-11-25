package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.IntervalEnumDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.IntervalEnumDto;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizersStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.TimeBlockSummaryResponseDto;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticsControllerUnitTests {
    private StatisticsController underTest;

    private StatisticsService statisticsService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        statisticsService = mock(StatisticsService.class);
        modelMapper = mock(ModelMapper.class);
        underTest = new StatisticsController(this.statisticsService, this.modelMapper);
    }

    @Test
    public void getOrganizersStatistics() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant endTest = Instant.parse("2020-11-30T00:00:00.000Z");

        String email = "bob@bob.com";
        String name = "Bob";
        OrganizerStatisticsResponseDto organizerStatisticsResponseDto = new OrganizerStatisticsResponseDto()
                .setOrganizerEmail("bob@bob.com")
                .setTotalCost(1.0)
                .setTotalHours(1)
                .setTotalMeetingCount(1);


        OrganizerStatisticsDm mockDm = OrganizerStatisticsDm.builder()
                .organizerEmail("bob@bob.com")
                .totalCost(1.0)
                .totalMeetingCount(1)
                .totalHours(1)
                .build();

        List<OrganizerStatisticsDm> dms = Lists.list(mockDm);
        List<OrganizerStatisticsResponseDto> dtos = Lists.list(organizerStatisticsResponseDto);
        when(statisticsService.getOrganizersStatistics(eq(startTest), eq(endTest), any(OptionalInt.class)))
                .thenReturn(dms);
        when(modelMapper.map(dms, new TypeToken<List<OrganizerStatisticsResponseDto>>() {
        }.getType()))
                .thenReturn(dtos);

//        OrganizersStatisticsResponseDto organizersStatisticsResponseDto = underTest
//                .getOrganizersStatistics(startTest, endTest, OptionalInt.empty());
        OrganizersStatisticsResponseDto organizersStatisticsResponseDto = underTest
                .getOrganizersStatistics(startTest, endTest);
        assertThat(organizersStatisticsResponseDto).isNotNull();
        assertThat(organizersStatisticsResponseDto.getOrganizersStatistics().size()).isEqualTo(1);
        assertThat(organizersStatisticsResponseDto.getOrganizersStatistics().get(0))
                .isEqualTo(organizerStatisticsResponseDto);

    }

    @Test
    public void getTimeBlockSummary() {
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");

        TimeBlockSummaryResponseDto mockDto = new TimeBlockSummaryResponseDto(
                UUID.randomUUID(),
                IntervalEnumDto.Day,
                1L,
                1L,
                1L,
                1L
        );

        TimeBlockSummaryDm mockDm = new TimeBlockSummaryDm(
                mockDto.getId(),
                IntervalEnumDm.Day,
                1L,
                1L,
                1L,
                1L

        );

        when(statisticsService.getTimeBlockSummary(any(ZonedDateTime.class), any(ZonedDateTime.class)))
                .thenReturn(mockDm);
        when(modelMapper.map(mockDm, TimeBlockSummaryResponseDto.class)).thenReturn(mockDto);

        TimeBlockSummaryResponseDto timeBlockSummaryResponseDto = underTest.getTimeBlockSummary(startTest, endTest);
        assertThat(timeBlockSummaryResponseDto).isNotNull();
    }


    @Test
    public void getPerDepartmentStatistics() {
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");

        DepartmentStatisticsDm departmentStatisticsDm = new DepartmentStatisticsDm(
                UUID.randomUUID(),
                "HR",
                1L,
                1L,
                1L,
                1L
        );

        List<DepartmentStatisticsDm> mockDmList = Lists.list(departmentStatisticsDm);

        DepartmentStatisticsResponseDto departmentStatisticsResponseDto = new DepartmentStatisticsResponseDto(
                UUID.randomUUID(),
                "HR",
                1L,
                1L,
                1L,
                1L
        );

        List<DepartmentStatisticsResponseDto> mockDtoList = Lists.list(departmentStatisticsResponseDto);

//        when(statisticsService.getPerDepartmentStatistics(any(ZonedDateTime.class), any(ZonedDateTime.class)))
//                .thenReturn(mockDmList);
//
//        List<DepartmentStatisticsDao> departmentStatisticsResponseDtoList = underTest.getPerDepartmentStatistics(startTest, endTest);
//        assertThat(departmentStatisticsResponseDtoList).isNotNull();
    }


    @Test
    public void getTrailingStatistics() {
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");
        List<TimeBlockSummaryDm> timeBlockSummaryDmList = Lists.list(
                new TimeBlockSummaryDm().setAverageCost(10).setId(UUID.randomUUID())
        );
        when(statisticsService.getTrailingStatistics(startTest, endTest, IntervalEnumDm.Month))
                .thenReturn(timeBlockSummaryDmList);


//        TrailingStatisticsResponseDto trailingStatisticsResponseDto = underTest.getTrailingStatistics(startTest, endTest, IntervalEnumDao.Month);
//        assertThat(trailingStatisticsResponseDto).isNotNull();
    }
}
