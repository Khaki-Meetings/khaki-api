package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.IntervalDe;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StatisticsServiceUnitTests {

    private StatisticsService underTest;
    private DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService;
    private TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService;
    private TimeBlockGeneratorFactory timeBlockGeneratorFactory;

    @BeforeEach
    public void setup() {
        departmentStatisticsPersistenceService = mock(DepartmentStatisticsPersistenceInterface.class);
        timeBlockSummaryPersistenceService = mock(TimeBlockSummaryPersistenceInterface.class);
        timeBlockGeneratorFactory = mock(TimeBlockGeneratorFactory.class);
        underTest = new StatisticsService(
                departmentStatisticsPersistenceService,
                timeBlockSummaryPersistenceService,
                timeBlockGeneratorFactory
        );
    }

    @Test
    public void getTimeBlockSummary() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant endTest = Instant.parse("2020-11-18T00:00:00.000Z");

        UUID id = UUID.randomUUID();
        TimeBlockSummaryDm timeBlockSummaryDm = new TimeBlockSummaryDm(1L, 1);


        when(timeBlockSummaryPersistenceService.getTimeBlockSummary(startTest, endTest)).thenReturn(timeBlockSummaryDm);

        TimeBlockSummaryDm timeBlockSummaryResponseDto = underTest.getTimeBlockSummary(startTest, endTest);
        assertThat(timeBlockSummaryResponseDto).isNotNull();
    }

    @Test
    public void getPerDepartmentStatistics() {
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");

        UUID id = UUID.randomUUID();
        DepartmentStatisticsDm departmentStatisticsDm = new DepartmentStatisticsDm(id, "department", 1L, 1L, 1L, 1L);
//        when(departmentStatisticsPersistenceService.getPerDepartmentStatistics(startTest, endTest)).thenReturn(departmentStatisticsDm);
//        DepartmentStatisticsDm departmentStatisticsResponseDmList = underTest.getPerDepartmentStatistics(startTest, endTest);
//        assertThat(departmentStatisticsResponseDmList).isNotNull();
    }

    @Test
    public void testTrailingStatisticsMonth() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        int count = 2;
        IntervalDe interval = IntervalDe.Month;

        ArgumentCaptor<Instant> startCaptor = ArgumentCaptor.forClass(Instant.class);
        ArgumentCaptor<Instant> endCaptor = ArgumentCaptor.forClass(Instant.class);
        when(timeBlockGeneratorFactory.get(any())).thenCallRealMethod();
        when(timeBlockSummaryPersistenceService.getTimeBlockSummary(any(Instant.class), any(Instant.class)))
                .thenReturn(new TimeBlockSummaryDm(1L, 1));

        underTest.getTrailingStatistics(startTest, interval, count);

        verify(timeBlockSummaryPersistenceService, times(2))
                .getTimeBlockSummary(startCaptor.capture(), endCaptor.capture());

        List<Instant> allStarts = startCaptor.getAllValues();
        List<Instant> allEnds = endCaptor.getAllValues();

        Instant firstPassedStartInstant = allStarts.get(0);
        Instant firstPassedEndInstant = allEnds.get(0);
        Instant firstStartShouldBe = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant firstEndShouldBe = Instant.parse("2020-11-30T23:59:59Z");

        assertThat(firstPassedStartInstant).isEqualTo(firstStartShouldBe);
        assertThat(firstPassedEndInstant).isEqualTo(firstEndShouldBe);

        Instant secondPassedStartInstant = allStarts.get(1);
        Instant secondPassedEndInstant = allEnds.get(1);
        Instant secondStartShouldBe = Instant.parse("2020-10-01T00:00:00Z");
        Instant secondEndShouldBe = Instant.parse("2020-10-30T23:59:59Z");

        assertThat(secondPassedStartInstant).isEqualTo(secondStartShouldBe);
        assertThat(secondPassedEndInstant).isEqualTo(secondEndShouldBe);
    }

}
