package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.*;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StatisticsServiceUnitTests {

    private StatisticsService underTest;
    private DepartmentStatisticsPersistenceInterface departmentStatisticsPersistenceService;
    private OrganizersStatisticsPersistenceInterface organizersStatisticsPersistenceService;
    private TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService;

    @BeforeEach
    public void setup() {
        departmentStatisticsPersistenceService = mock(DepartmentStatisticsPersistenceInterface.class);
        organizersStatisticsPersistenceService = mock(OrganizersStatisticsPersistenceInterface.class);
        timeBlockSummaryPersistenceService = mock(TimeBlockSummaryPersistenceInterface.class);
        underTest = new StatisticsService(departmentStatisticsPersistenceService, organizersStatisticsPersistenceService, timeBlockSummaryPersistenceService);
    }

    @Test
    public void getOrganizersStatistics() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant endTest = Instant.parse("2020-11-12T00:00:00.000Z");

        OrganizerStatisticsDm organizerStatisticsDm = new OrganizerStatisticsDm(
                "bob@bob.com",
                1,
                1.0,
                1
        );
        when(organizersStatisticsPersistenceService.getOrganizersStatistics(eq(startTest), eq(endTest), any(OptionalInt.class)))
                .thenReturn(Lists.list(organizerStatisticsDm));

        List<OrganizerStatisticsDm> organizersStatistics = underTest.getOrganizersStatistics(startTest, endTest, OptionalInt.empty());
        assertThat(organizersStatistics).isNotNull();
        assertThat(organizersStatistics.size()).isEqualTo(1);
        assertThat(organizersStatistics.get(0)).isEqualTo(organizerStatisticsDm);

    }

    @Test
    public void getTimeBlockSummary() {


        EmailDm emailDm = new EmailDm("test", new DomainDm("mail"));
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant endTest = Instant.parse("2020-11-18T00:00:00.000Z");

        UUID id = UUID.randomUUID();
        TimeBlockSummaryDm timeBlockSummaryDm = new TimeBlockSummaryDm(1, 1);


        when(timeBlockSummaryPersistenceService.getTimeBlockSummary(startTest, endTest)).thenReturn(timeBlockSummaryDm);

        TimeBlockSummaryDm timeBlockSummaryResponseDto = underTest.getTimeBlockSummary(startTest, endTest);
        assertThat(timeBlockSummaryResponseDto).isNotNull();


    }

    @Test
    public void getPerDepartmentStatistics() {
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");

        UUID id = UUID.randomUUID();
        DepartmentStatisticsDm departmentStatisticsDm = new DepartmentStatisticsDm(id, "department", 1, 1, 1, 1);
//        when(departmentStatisticsPersistenceService.getPerDepartmentStatistics(startTest, endTest)).thenReturn(departmentStatisticsDm);
//        DepartmentStatisticsDm departmentStatisticsResponseDmList = underTest.getPerDepartmentStatistics(startTest, endTest);
//        assertThat(departmentStatisticsResponseDmList).isNotNull();
    }

    @Test
    public void testTrailingStatisticsMonth() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        int count = 2;
        IntervalEnumDm interval = IntervalEnumDm.Month;


        ArgumentCaptor<Instant> startCaptor = ArgumentCaptor.forClass(Instant.class);
        ArgumentCaptor<Instant> endCaptor = ArgumentCaptor.forClass(Instant.class);
        when(timeBlockSummaryPersistenceService.getTimeBlockSummary(any(Instant.class), any(Instant.class)))
                .thenReturn(new TimeBlockSummaryDm(1, 1));

        underTest.getTrailingStatistics(startTest, interval, count);

        verify(timeBlockSummaryPersistenceService).getTimeBlockSummary(startCaptor.capture(), endCaptor.capture());

        Instant firstPassedStartInstant = startCaptor.getValue();
        Instant firstPassedEndInstant = endCaptor.getValue();
        Instant firstStartShouldBe = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant firstEndShouldBe = Instant.parse("2020-11-30T23:59:59.999Z");

        assertThat(firstPassedStartInstant).isEqualTo(firstStartShouldBe);
        assertThat(firstPassedEndInstant).isEqualTo(firstEndShouldBe);

        Instant secondPassedStartInstant = startCaptor.getValue();
        Instant secondPassedEndInstant = endCaptor.getValue();
        Instant secondStartShouldBe = Instant.parse("2020-12-01T00:00:00.000Z");
        Instant secondEndShouldBe = Instant.parse("2020-12-31T23:59:59.999Z");

        assertThat(secondPassedStartInstant).isEqualTo(secondStartShouldBe);
        assertThat(secondPassedEndInstant).isEqualTo(secondEndShouldBe);
    }

}
