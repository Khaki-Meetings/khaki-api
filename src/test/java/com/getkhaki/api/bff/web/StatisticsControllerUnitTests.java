package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.*;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.persistence.models.*;
import com.getkhaki.api.bff.web.models.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
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
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");

        String email = "bob@bob.com";
        String name = "Bob";
        OrganizerStatisticsResponseDto organizerStatisticsResponseDto = new OrganizerStatisticsResponseDto()
                .setOrganizer(
                        new OrganizerDto()
                                .setEmail(email)
                                .setName(name)
                )
                .setTotalCost(1)
                .setTotalHours(1)
                .setTotalMeetings(1);


        OrganizerStatisticsDm mockDm = OrganizerStatisticsDm.builder()
                .id(UUID.randomUUID())
                .organizer(
                        OrganizerDm.builder()
                                .name(name)
                                .email(email)
                                .build()
                )
                .totalCost(1)
                .totalMeetings(1)
                .totalHours(1)
                .build();

        int count = 1;
        List<OrganizerStatisticsDm> dms = Lists.list(mockDm);
        List<OrganizerStatisticsResponseDto> dtos = Lists.list(organizerStatisticsResponseDto);
        when(statisticsService.getOrganizerStatistics(eq(startTest), eq(endTest), anyInt())).thenReturn(dms);
        when(modelMapper.map(dms,  new TypeToken<List<OrganizerStatisticsResponseDto>>(){}.getType()))
                .thenReturn(dtos);

        OrganizersStatisticsResponseDto organizersStatisticsResponseDto = underTest
                .getOrganizersStatistics(startTest, endTest, OptionalInt.empty());
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
                IntervalEnum.Day,
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

        when(statisticsService.getPerDepartmentStatistics(any(ZonedDateTime.class), any(ZonedDateTime.class)))
                .thenReturn(mockDmList);

        List<DepartmentStatisticsDao> departmentStatisticsResponseDtoList = underTest.getPerDepartmentStatistics(startTest, endTest);
        assertThat(departmentStatisticsResponseDtoList).isNotNull();
    }


    @Test
    public void getTrailingStatistics() {
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");

        TrailingStatisticsDao trailingStatisticsResponseDto = underTest.getTrailingStatistics(startTest, endTest, IntervalEnumDao.Interval1);
        assertThat(trailingStatisticsResponseDto).isNotNull();
    }
}
