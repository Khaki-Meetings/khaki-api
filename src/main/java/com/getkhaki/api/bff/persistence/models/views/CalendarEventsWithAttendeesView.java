package com.getkhaki.api.bff.persistence.models.views;

import com.getkhaki.api.bff.persistence.models.CalendarEventParticipantDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;

import java.time.Instant;
import java.util.List;

public interface CalendarEventsWithAttendeesView {
    String getGoogleCalendarId();
    String getSummary();
    Instant getCreated();
    Instant getStart();
    Instant getEnd();
    List<PersonDaoExt> getParticipants();
}
