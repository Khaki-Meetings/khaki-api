package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.IntervalDe;
import org.springframework.stereotype.Service;

@Service
public class TimeBlockGeneratorFactory {

    TimeBlockGeneratorInterface get(IntervalDe interval) {
        switch (interval) {
            case Day:
                return new DayTimeBlockGenerator();
            case Week:
                return new WeekTimeBlockGenerator();
            case Month:
                return new MonthTimeBlockGenerator();
            case Year:
            default:
                throw new IllegalStateException("Unexpected value: " + interval);
        }
    }
}
