package com.getkhaki.api.bff.persistence.repositories.test_data;

import com.getkhaki.api.bff.persistence.models.CalendarEventParticipantDao;
import org.assertj.core.util.Lists;

import java.util.List;

public class CalendarEventParticipantTestData implements TestDataInterface<CalendarEventParticipantDao> {
    @Override
    public List<CalendarEventParticipantDao> getData() {
        return Lists.list(
                new CalendarEventParticipantDao()
                        .setEmail(new EmailsTestData().getData().get(0))
                        .setCalendarEvent(new CalendarEventTestData().getData().get(0))
                        .setParticipantType(new ParticipantTypeData().getData().get(0)),
                new CalendarEventParticipantDao()
                        .setEmail(new EmailsTestData().getData().get(1))
                        .setCalendarEvent(new CalendarEventTestData().getData().get(0))
                        .setParticipantType(new ParticipantTypeData().getData().get(1))
        );
    }
}
