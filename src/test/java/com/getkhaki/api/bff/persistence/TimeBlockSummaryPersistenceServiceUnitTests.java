package com.getkhaki.api.bff.persistence;


import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.services.TimeBlockSummaryPersistenceService;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;
import com.getkhaki.api.bff.persistence.models.TimeBlockSummaryDao;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class TimeBlockSummaryPersistenceServiceUnitTests {

    private TimeBlockSummaryPersistenceService underTest;

    @Mock
    private TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        underTest = new TimeBlockSummaryPersistenceService(modelMapper, timeBlockSummaryRepositoryInterface);
    }

    @Test
    public void test() {

        ZonedDateTime startTest = ZonedDateTime.parse("2019-03-27T10:15:30");
        ZonedDateTime endTest = ZonedDateTime.now();
        UUID id = UUID.randomUUID();
        TimeBlockSummaryDm timeBlockSummaryDm = new TimeBlockSummaryDm(id, IntervalEnumDao.Interval1,1,1,1,1);
        TimeBlockSummaryDao timeBlockSummaryDao = new TimeBlockSummaryDao(timeBlockSummaryDm.getId(),timeBlockSummaryDm.getInterval(),timeBlockSummaryDm.getTotalTime(),timeBlockSummaryDm.getTotalCost(),timeBlockSummaryDm.getAverageCost(),timeBlockSummaryDm.getMeetingCount());
        List<TimeBlockSummaryDao> timeBlockListDm= Lists.list(timeBlockSummaryDao);

        when(modelMapper.map(timeBlockSummaryDm, TimeBlockSummaryDao.class)).thenReturn(timeBlockSummaryDao);
        when(timeBlockSummaryRepositoryInterface.findTimeBlockSummaryInRange(startTest,endTest)).thenReturn(timeBlockSummaryDao);
        when(timeBlockSummaryRepositoryInterface.findTimeBlockSummaryInRangeWithInterval(startTest,endTest,IntervalEnumDao.Interval1)).thenReturn(timeBlockListDm);


        TimeBlockSummaryDm ret = underTest.getTimeBlockSummary(startTest,endTest);
        assertThat(ret).isNotNull();

        List<TimeBlockSummaryDm> ret2 = underTest.getTrailingStatistics(startTest,endTest,IntervalEnumDao.Interval1);
        assertThat(ret2).isNotNull();
    }
}
