package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.*;
import com.getkhaki.api.bff.persistence.models.*;
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
    private  TimeBlockSummaryPersistenceService timeBlockSummaryPersistenceService;

    @BeforeEach
    public void setup() {
        departmentStatisticsPersistenceService = mock(DepartmentStatisticsPersistenceService.class);
        organizersStatisticsPersistenceService = mock(OrganizersStatisticsPersistenceService.class);
        timeBlockSummaryPersistenceService = mock(TimeBlockSummaryPersistenceService.class);
        underTest = new StatisticsService(departmentStatisticsPersistenceService,organizersStatisticsPersistenceService,timeBlockSummaryPersistenceService);
    }

    @Test
    public void test() {


        EmailDm emailDm=new EmailDm("test",new DomainTypeDm("mail"));
        ZonedDateTime startTest = ZonedDateTime.parse("2019-03-27T10:15:30");
        ZonedDateTime endTest = ZonedDateTime.now();

        UUID id = UUID.randomUUID();
        OrganizersStatisticsDm organizersStatisticsDm = new OrganizersStatisticsDm(id,emailDm,1,1,1);
        TimeBlockSummaryDm timeBlockSummaryDm=new TimeBlockSummaryDm(id,IntervalEnumDao.Interval1,1,1,1,1);
        DepartmentStatisticsDm departmentStatisticsDm=new DepartmentStatisticsDm(id,"department",1,1,1,1);
        List<TimeBlockSummaryDm> trailingListDm= Lists.list(timeBlockSummaryDm);


        when(organizersStatisticsPersistenceService.getOrganizerStatistics(emailDm.getEmail())).thenReturn(organizersStatisticsDm);
        when(timeBlockSummaryPersistenceService.getTimeBlockSummary(startTest,endTest)).thenReturn(timeBlockSummaryDm);
        when(timeBlockSummaryPersistenceService.getTrailingStatistics(startTest,endTest,IntervalEnumDao.Interval1)).thenReturn(trailingListDm);
        when(departmentStatisticsPersistenceService.getPerDepartmentStatistics(startTest,endTest)).thenReturn(departmentStatisticsDm);




        OrganizersStatisticsDm organizersStatisticsResponse = underTest.getOrganizerStatistics(emailDm.getEmail());
        assertThat(organizersStatisticsResponse).isNotNull();

        //------------------------------------------------------

        TimeBlockSummaryDm timeBlockSummaryResponseDto = underTest.getTimeBlockSummary(startTest,endTest);
        assertThat(timeBlockSummaryResponseDto).isNotNull();

        //-------------------------------------------------------

        DepartmentStatisticsDm departmentStatisticsResponseDmList = underTest.getPerDepartmentStatistics(startTest,endTest);
        assertThat(departmentStatisticsResponseDmList).isNotNull();

        //--------------------------------------------------------

        List<TimeBlockSummaryDm> trailingStatisticsResponseDm =  underTest.getTrailingStatistics(startTest,endTest, IntervalEnumDao.Interval1);
        assertThat(trailingStatisticsResponseDm).isNotNull();


    }


}
