package com.getkhaki.api.bff.persistence.models.views;

import com.getkhaki.api.bff.web.models.PersonDto;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.Instant;
import java.util.List;

public interface CalendarEventsWithAttendeesViewInterface {
    byte[] getId();
    String getGoogleCalendarId();
    String getSummary();
    Instant getCreated();
    Instant getStart();
    Instant getEnd();
    Integer getNumberInternalAttendees();
    List<PersonDto> getParticipants();
    Integer getTotalSeconds();
}
