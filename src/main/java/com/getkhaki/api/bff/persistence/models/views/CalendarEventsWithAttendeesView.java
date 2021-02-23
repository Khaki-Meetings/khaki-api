package com.getkhaki.api.bff.persistence.models.views;

import com.getkhaki.api.bff.web.models.PersonDto;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.Instant;
import java.util.List;

public interface CalendarEventsWithAttendeesView {
    byte[] getId();
    String getGoogleCalendarId();
    String getSummary();
    Instant getCreated();
    Instant getStart();
    Instant getEnd();
    String getOrganizerEmail();
    String getOrganizerFirstName();
    String getOrganizerLastName();
    Integer getNumberInternalAttendees();
    Integer getTotalSeconds();
}
