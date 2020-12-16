package com.getkhaki.api.bff.domain.services;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class MonthTimeBlockGenerator extends BaseTimeBlockGenerator {
    @Override
    public Instant addUnit(Instant start) {
        return start.atOffset(ZoneOffset.UTC)
                .plus(1, ChronoUnit.MONTHS)
                .toInstant();
    }

    @Override
    public Instant minusUnit(Instant instant) {
        return instant.atOffset(ZoneOffset.UTC)
                .minus(1, ChronoUnit.MONTHS)
                .toInstant();
    }
}
