package com.getkhaki.api.bff.domain.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DayTimeBlockGenerator extends BaseTimeBlockGenerator {
    @Override
    public Instant addUnit(Instant start) {
        return start.plus(1, ChronoUnit.DAYS);
    }

    @Override
    public Instant minusUnit(Instant instant) {
        return instant.minus(1, ChronoUnit.DAYS);
    }
}
