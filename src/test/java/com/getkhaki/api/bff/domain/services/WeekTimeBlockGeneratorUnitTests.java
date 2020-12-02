package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.TimeBlockRangeDm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WeekTimeBlockGeneratorUnitTests {
    private WeekTimeBlockGenerator underTest;

    @BeforeEach
    public void setup() {
        underTest = new WeekTimeBlockGenerator();
    }

    @Test
    public void testTimeBlockGeneration() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        int count = 2;

        List<TimeBlockRangeDm> timeBlockList = underTest.generate(startTest, count);

        assertThat(timeBlockList).hasSize(count);
        Instant firstStart = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant firstEnd = Instant.parse("2020-11-07T23:59:59.999Z");
        assertThat(timeBlockList.get(0).getStart()).isEqualTo(firstStart);
        assertThat(timeBlockList.get(0).getEnd()).isEqualTo(firstEnd);

        Instant secondStart = Instant.parse("2020-11-08T00:00:00.000Z");
        Instant secondEnd = Instant.parse("2020-11-14T23:59:59.999Z");
        assertThat(timeBlockList.get(1).getStart()).isEqualTo(secondStart);
        assertThat(timeBlockList.get(1).getEnd()).isEqualTo(secondEnd);
    }
}
