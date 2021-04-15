package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.*;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.web.models.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatisticsControllerUnitTests {
    private StatisticsController underTest;

    @Mock
    private StatisticsService statisticsService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private OrganizersStatisticsPersistenceInterface organizersStatisticsPersistenceService;
    @Mock
    private TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService;
    @Mock
    private DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService;

    @BeforeEach
    public void setup() {
        underTest = new StatisticsController(
                this.statisticsService,
                organizersStatisticsPersistenceService,
                timeBlockSummaryPersistenceService,
                departmentStatisticsPersistenceService,
                goalPersistenceService, this.modelMapper
        );
    }

    @Test
    public void getOrganizersStatistics() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant endTest = Instant.parse("2020-11-30T00:00:00.000Z");

        OrganizerStatisticsDm mockDm = OrganizerStatisticsDm.builder()
                .organizerEmail("bob@bob.com")
                .totalCost(1.0)
                .totalMeetings(1)
                .totalSeconds(1L)
                .build();

        PageImpl<OrganizerStatisticsDm> dms = new PageImpl<>(Lists.list(mockDm));

        var dto = OrganizerStatisticsResponseDto.builder()
                .organizerEmail("bob@bob.com")
                .totalCost(1.0)
                .totalSeconds(1L)
                .totalMeetings(1)
                .build();

        Pageable pageable = PageRequest.of(0, 2);
        when(organizersStatisticsPersistenceService.getOrganizersStatistics(
                eq(startTest), eq(endTest), eq(pageable), eq(StatisticsFilterDe.Internal)
        )).thenReturn(dms);


        when(modelMapper.map(StatisticsFilterDte.Internal, StatisticsFilterDe.class))
                .thenReturn(StatisticsFilterDe.Internal);
        when(modelMapper.map(mockDm, OrganizerStatisticsResponseDto.class))
                .thenReturn(dto);

        Page<OrganizerStatisticsResponseDto> response = underTest
                .getOrganizersStatistics(startTest, endTest, Optional.of(StatisticsFilterDte.Internal), pageable);

        assertThat(response).isNotNull();
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.get().findFirst().orElseThrow()).isEqualTo(dto);
    }

    @Test
    public void getOrganizersStatisticsAggregate() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant endTest = Instant.parse("2020-11-30T00:00:00.000Z");

        OrganizerStatisticsAggregateDm mockDm = OrganizerStatisticsAggregateDm.builder()
                .organizerEmail("bob@bob.com")
                .internalMeetingSeconds(2000)
                .internalMeetingCount(2)
                .externalMeetingSeconds(3000)
                .externalMeetingCount(3)
                .build();

        PageImpl<OrganizerStatisticsAggregateDm> dms = new PageImpl<>(Lists.list(mockDm));

        var dto = OrganizerStatisticsAggregateResponseDto.builder()
                .organizerEmail("bob@bob.com")
                .internalMeetingSeconds(2000)
                .internalMeetingCount(2)
                .externalMeetingSeconds(3000)
                .externalMeetingCount(3)
                .build();

        Pageable pageable = PageRequest.of(0, 2);
        when(organizersStatisticsPersistenceService.getAggregateOrganizersStatistics(
                eq(startTest), eq(endTest), eq(pageable))
        ).thenReturn(dms);

        when(modelMapper.map(mockDm, OrganizerStatisticsAggregateResponseDto.class))
                .thenReturn(dto);

        Page<OrganizerStatisticsAggregateResponseDto> response = underTest
                .getAggregateOrganizersStatistics(startTest, endTest, pageable);

        assertThat(response).isNotNull();
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.get().findFirst().orElseThrow()).isEqualTo(dto);
    }

    @Test
    public void getTimeBlockSummary() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant endTest = Instant.parse("2020-11-30T00:00:00.000Z");

        TimeBlockSummaryResponseDto mockDto = new TimeBlockSummaryResponseDto(
                1L,
                1,
                Instant.now(),
                Instant.now(),
                0,
                0,
                0,
                0,
                0L,
                0
        );

        TimeBlockSummaryDm mockDm = new TimeBlockSummaryDm(
                1L,
                1,
                Instant.now(),
                Instant.now(),
                0,
                0,
                0,
                0,
                0L,
                0
        );

        when(modelMapper.map(StatisticsFilterDte.External, StatisticsFilterDe.class))
                .thenReturn(StatisticsFilterDe.External);
        when(timeBlockSummaryPersistenceService.getTimeBlockSummary(any(Instant.class), any(Instant.class), any()))
                .thenReturn(mockDm);
        when(modelMapper.map(mockDm, TimeBlockSummaryResponseDto.class)).thenReturn(mockDto);

        TimeBlockSummaryResponseDto timeBlockSummaryResponseDto = underTest.getTimeBlockSummary(
                startTest,
                endTest,
                Optional.of(StatisticsFilterDte.External)
        );
        assertThat(timeBlockSummaryResponseDto).isNotNull();
    }


    @Test
    public void getPerDepartmentStatistics() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant endTest = Instant.parse("2020-11-30T00:00:00.000Z");

        DepartmentStatisticsDm departmentStatisticsDm = new DepartmentStatisticsDm(
                UUID.randomUUID(),
                "HR",
                1L,
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
                1L
        );

        List<DepartmentStatisticsResponseDto> mockDtoList = Lists.list(departmentStatisticsResponseDto);
        var ret = underTest.getPerDepartmentStatistics(
                startTest, endTest, Optional.of(StatisticsFilterDte.External)
        );
    }


    @Test
    public void getTrailingStatistics() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        List<TimeBlockSummaryDm> timeBlockSummaryDmList = Lists.list(
                new TimeBlockSummaryDm().setMeetingCount(1).setTotalSeconds(1L)
        );

        when(modelMapper.map(StatisticsFilterDte.External, StatisticsFilterDe.class))
                .thenReturn(StatisticsFilterDe.External);
        when(statisticsService.getTrailingStatistics(startTest, IntervalDe.Month, 1, StatisticsFilterDe.External))
                .thenReturn(timeBlockSummaryDmList);

        underTest.getTrailingStatistics(startTest, IntervalDe.Month, 1, Optional.of(StatisticsFilterDte.External));

        verify(
                statisticsService, times(1)
        ).getTrailingStatistics(startTest, IntervalDe.Month, 1, StatisticsFilterDe.External);
    }

    @Test
    public void getIndividualStatistics() {
        var employeeId = UUID.randomUUID();
        var start = Instant.now();
        var end = Instant.now();
        var externalFilterDte = Optional.of(StatisticsFilterDte.External);
        var internalFilterDte = Optional.of(StatisticsFilterDte.Internal);
        var externalFilterDe = StatisticsFilterDe.External;
        var internalFilterDe = StatisticsFilterDe.Internal;
        var externalTimeBlockSummaryDm = mock(TimeBlockSummaryDm.class);
        var internalTimeBlockSummaryDm = mock(TimeBlockSummaryDm.class);
        var externalTimeBlockSummaryResponseDto = mock(TimeBlockSummaryResponseDto.class);
        var internalTimeBlockSummaryResponseDto = mock(TimeBlockSummaryResponseDto.class);

        when(modelMapper.map(StatisticsFilterDte.External, StatisticsFilterDe.class))
                .thenReturn(externalFilterDe);

        when(modelMapper.map(StatisticsFilterDte.Internal, StatisticsFilterDe.class))
                .thenReturn(internalFilterDe);

        when(modelMapper.map(StatisticsFilterDte.Internal, StatisticsFilterDe.class))
                .thenReturn(StatisticsFilterDe.Internal);

        when(timeBlockSummaryPersistenceService.getIndividualTimeBlockSummary(employeeId, start, end, externalFilterDe))
                .thenReturn(externalTimeBlockSummaryDm);

        when(timeBlockSummaryPersistenceService.getIndividualTimeBlockSummary(employeeId, start, end, internalFilterDe))
                .thenReturn(internalTimeBlockSummaryDm);

        when(modelMapper.map(externalTimeBlockSummaryDm, TimeBlockSummaryResponseDto.class))
                .thenReturn(externalTimeBlockSummaryResponseDto);

        when(modelMapper.map(internalTimeBlockSummaryDm, TimeBlockSummaryResponseDto.class))
                .thenReturn(internalTimeBlockSummaryResponseDto);

        TimeBlockSummaryResponseDto externalResult = this.underTest.getIndividualStatistics(
                employeeId, start, end, externalFilterDte);

        assertThat(externalResult).isEqualTo(externalTimeBlockSummaryResponseDto);

        TimeBlockSummaryResponseDto internalResult = this.underTest.getIndividualStatistics(
                employeeId, start, end, internalFilterDte);

        assertThat(internalResult).isEqualTo(internalTimeBlockSummaryResponseDto);
    }
}
