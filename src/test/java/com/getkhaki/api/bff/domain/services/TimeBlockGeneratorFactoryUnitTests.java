package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.IntervalDe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeBlockGeneratorFactoryUnitTests {
    private TimeBlockGeneratorFactory underTest;

    @BeforeEach
    public void setup() {
        underTest = new TimeBlockGeneratorFactory();
    }

    @Test
    public void testDayFactory() {
        TimeBlockGeneratorInterface generator = underTest.get(IntervalDe.Day);
        assertThat(generator).isInstanceOf(DayTimeBlockGenerator.class);
    }

    @Test
    public void testWeekFactory() {
        TimeBlockGeneratorInterface generator = underTest.get(IntervalDe.Week);
        assertThat(generator).isInstanceOf(WeekTimeBlockGenerator.class);
    }

    @Test
    public void testMonthFactory() {
        TimeBlockGeneratorInterface generator = underTest.get(IntervalDe.Month);
        assertThat(generator).isInstanceOf(MonthTimeBlockGenerator.class);
    }
}
