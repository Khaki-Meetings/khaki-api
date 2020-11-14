package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.*;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticsServicesUnitTests {

    private StatisticsService underTest;
    private DepartmentStatisticsPersistenceService departmentStatisticsPersistenceService;
    private OrganizersStatisticsPersistenceService organizersStatisticsPersistenceService;
    private TimeBlockSummaryPersistenceService timeBlockSummaryPersistenceService;

    @BeforeEach
    public void setup() {
        departmentStatisticsPersistenceService = mock(DepartmentStatisticsPersistenceService.class);
        organizersStatisticsPersistenceService = mock(OrganizersStatisticsPersistenceService.class);
        timeBlockSummaryPersistenceService = mock(TimeBlockSummaryPersistenceService.class);
        underTest = new StatisticsService(departmentStatisticsPersistenceService, organizersStatisticsPersistenceService, timeBlockSummaryPersistenceService);
    }

    @Test
    public void getOrganizerStatistics() {


        EmailDm emailDm = new EmailDm("test", new DomainTypeDm("mail"));

        UUID id = UUID.randomUUID();
//        OrganizerStatisticsDm organizerStatisticsDm = new OrganizerStatisticsDm(id, "test@test.com", 1, 1, 1);
//        when(organizersStatisticsPersistenceService.getOrganizerStatistics(, emailDm.getEmail(), , )).thenReturn(organizersStatisticsDm);

//        OrganizersStatisticsDm organizersStatisticsResponse = underTest.getOrganizerStatistics(, emailDm.getEmail(), , );
//        assertThat(organizersStatisticsResponse).isNotNull();

    }


    @Test
    public void getTimeBlockSummary() {


        EmailDm emailDm = new EmailDm("test", new DomainTypeDm("mail"));
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
        when(timeBlockSummaryPersistenceService.getTrailingStatistics(startTest, endTest, IntervalEnumDao.Interval1)).thenReturn(trailingListDm);
        List<TimeBlockSummaryDm> trailingStatisticsResponseDm = underTest.getTrailingStatistics(startTest, endTest, IntervalEnumDao.Interval1);
        assertThat(trailingStatisticsResponseDm).isNotNull();


    }

}
