package com.getkhaki.api.bff.persistence.models.views;

import com.getkhaki.api.bff.web.models.PersonDto;

import java.time.Instant;
import java.util.List;

public interface CalendarEventsWithAttendeesViewInterface {
    byte[] getId();
    String getGoogleCalendarId();
    String getSummary();
    Instant getCreated();
    Instant getStart();
    Instant getEnd();
    List<PersonDto> getParticipants();
}
