package com.getkhaki.api.bff.domain.services;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class WeekTimeBlockGenerator extends BaseTimeBlockGenerator {
    @Override
    public Instant addUnit(Instant start) {
        return start.atOffset(ZoneOffset.UTC)
                .plus(1, ChronoUnit.WEEKS)
                .toInstant();
    }
}
