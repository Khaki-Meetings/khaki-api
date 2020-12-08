package com.getkhaki.api.bff.persistence;


import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.UUID;

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
    public void findTimeBlockSummaryInRange() {
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");
        UUID id = UUID.randomUUID();
        TimeBlockSummaryDm timeBlockSummaryDm = new TimeBlockSummaryDm(1, 1);
//        TimeBlockSummaryDao timeBlockSummaryDao = new TimeBlockSummaryDao(timeBlockSummaryDm.getId(), IntervalEnumDao.Interval1, timeBlockSummaryDm.getTotalTime(), timeBlockSummaryDm.getTotalCost(), timeBlockSummaryDm.getAverageCost(), timeBlockSummaryDm.getMeetingCount());
//
//        Mockito.lenient().when(modelMapper.map(timeBlockSummaryDao, TimeBlockSummaryDm.class)).thenReturn(timeBlockSummaryDm);
//        Mockito.lenient().when(timeBlockSummaryRepositoryInterface.findTimeBlockSummaryInRange(startTest, endTest)).thenReturn(timeBlockSummaryDao);
//
//
//        TimeBlockSummaryDm ret = underTest.getTimeBlockSummary(startTest, endTest);
//        assertThat(ret).isNotNull();

    }

    @Test
    public void getTrailingStatistics() {

//        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
//        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");
//        UUID id = UUID.randomUUID();
//        TimeBlockSummaryDm timeBlockSummaryDm = new TimeBlockSummaryDm(id, IntervalEnumDm.Day, 1, 1, 1, 1);
//        TimeBlockSummaryDao timeBlockSummaryDao = new TimeBlockSummaryDao(
//                timeBlockSummaryDm.getId(),
//                IntervalEnumDao.Interval1,
//                timeBlockSummaryDm.getTotalTime(),
//                timeBlockSummaryDm.getTotalCost(),
//                timeBlockSummaryDm.getAverageCost(),
//                timeBlockSummaryDm.getMeetingCount()
//        );
//        List<TimeBlockSummaryDao> timeBlockListDao = Lists.list(timeBlockSummaryDao);
//        List<TimeBlockSummaryDm> timeBlockListDm = Lists.list(timeBlockSummaryDm);
//
//        Mockito.lenient().when(
//                modelMapper.map(timeBlockListDao, new TypeToken<List<TimeBlockSummaryDm>>() {}.getType())
//        ).thenReturn(timeBlockListDm);
//
//        Mockito.lenient().when(timeBlockSummaryRepositoryInterface.findTimeBlockSummaryInRange(startTest, endTest))
//                .thenReturn(timeBlockSummaryDao);
//        Mockito.lenient().when(
//                timeBlockSummaryRepositoryInterface.findTimeBlockSummaryInRangeWithInterval(
//                        startTest,
//                        endTest,
//                        IntervalEnumDao.Interval1
//                )
//        ).thenReturn(timeBlockListDao);
//
//        List<TimeBlockSummaryDm> ret2 = underTest.getTrailingStatistics(startTest, endTest, IntervalEnumDao.Interval1);
//        assertThat(ret2).isNotNull();
    }
}
