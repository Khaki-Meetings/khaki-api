package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.IntervalDe;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.domain.services.StatisticsService;
import com.getkhaki.api.bff.web.models.DepartmentStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import com.getkhaki.api.bff.web.models.StatisticsFilterDte;
import com.getkhaki.api.bff.web.models.TimeBlockSummaryResponseDto;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
                this.modelMapper
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
        assertThat(response.get().findFirst().get()).isEqualTo(dto);
    }

    @Test
    public void getTimeBlockSummary() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant endTest = Instant.parse("2020-11-30T00:00:00.000Z");

        TimeBlockSummaryResponseDto mockDto = new TimeBlockSummaryResponseDto(
                1L,
                1
        );

        TimeBlockSummaryDm mockDm = new TimeBlockSummaryDm(
                1L,
                1
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
                1L
        );

        List<DepartmentStatisticsDm> mockDmList = Lists.list(departmentStatisticsDm);

        DepartmentStatisticsResponseDto departmentStatisticsResponseDto = new DepartmentStatisticsResponseDto(
                UUID.randomUUID(),
                "HR",
                1L
        );

        List<DepartmentStatisticsResponseDto> mockDtoList = Lists.list(departmentStatisticsResponseDto);
        var ret = underTest.getPerDepartmentStatistics(
                startTest, endTest, StatisticsFilterDte.External
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
}
