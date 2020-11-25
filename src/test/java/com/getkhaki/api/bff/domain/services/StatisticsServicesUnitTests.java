package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.*;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

public class StatisticsServicesUnitTests {

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
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        Instant endTest = Instant.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");
        int count = 5;

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
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");

        UUID id = UUID.randomUUID();
        TimeBlockSummaryDm timeBlockSummaryDm = new TimeBlockSummaryDm(id, IntervalEnumDm.Day, 1, 1, 1, 1);


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
    public void test() {
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");
        UUID id = UUID.randomUUID();
        TimeBlockSummaryDm timeBlockSummaryDm = new TimeBlockSummaryDm(id, IntervalEnumDm.Day, 1, 1, 1, 1);
        List<TimeBlockSummaryDm> trailingListDm = Lists.list(timeBlockSummaryDm);
        when(timeBlockSummaryPersistenceService.getTimeBlockSummary(startTest, endTest)).thenReturn(timeBlockSummaryDm);
        when(timeBlockSummaryPersistenceService.getTrailingStatistics(startTest, endTest, IntervalEnumDm.Day)).thenReturn(trailingListDm);
        List<TimeBlockSummaryDm> trailingStatisticsResponseDm = underTest.getTrailingStatistics(startTest, endTest, IntervalEnumDm.Day);
        assertThat(trailingStatisticsResponseDm).isNotNull();


    }

}
