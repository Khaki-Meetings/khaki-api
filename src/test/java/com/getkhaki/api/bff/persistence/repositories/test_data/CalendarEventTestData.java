package com.getkhaki.api.bff.persistence.repositories.test_data;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import org.assertj.core.util.Lists;

import java.time.ZonedDateTime;
import java.util.List;

public class CalendarEventTestData implements TestDataInterface<CalendarEventDao> {
    @Override
    public List<CalendarEventDao> getData() {
        return Lists.list(
                new CalendarEventDao().setSummary("event 1")
                .setCreated(ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]"))
        );
    }
}
