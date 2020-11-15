package com.getkhaki.api.bff.persistence.repositories.test_data;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import org.assertj.core.util.Lists;

import java.util.List;

public class CalendarEventTestData implements TestDataInterface<CalendarEventDao> {
    @Override
    public List<CalendarEventDao> getData() {
        return Lists.list(
                new CalendarEventDao().setSummary("event 1")
        );
    }
}
