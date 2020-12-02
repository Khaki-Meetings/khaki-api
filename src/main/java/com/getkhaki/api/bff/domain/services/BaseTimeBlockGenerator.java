package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.TimeBlockRangeDm;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTimeBlockGenerator implements TimeBlockGeneratorInterface {
    @Override
    public List<TimeBlockRangeDm> generate(Instant start, int count) {
        ZonedDateTime zonedStart = start.atZone(ZoneOffset.UTC);
        int hour = zonedStart.getHour();
        int minute = zonedStart.getMinute();
        int second = zonedStart.getSecond();
        int nano = zonedStart.getNano();
        if (hour + minute + second + nano != 0) {
            throw new RuntimeException(String.format("%s is not at start of day", start));
        }

        List<TimeBlockRangeDm> timeBlockRangeList = new ArrayList<>();
        Instant newStart = start.atZone(ZoneOffset.UTC).toInstant();
        Instant newEnd = addUnit(newStart).minus(1, ChronoUnit.MILLIS);
        for (int i = 0; i < count; i++) {
            timeBlockRangeList.add(
                    new TimeBlockRangeDm()
                            .setStart(newStart)
                            .setEnd(newEnd)
            );

            newStart = addUnit(newStart);
            newEnd = addUnit(newStart).minus(1, ChronoUnit.MILLIS);
        }

        return timeBlockRangeList;
    }

    abstract public Instant addUnit(Instant instant);
}
