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
  //  @Column(name = "MEETINGSUMMARY", length = 65535)
//    @Type(type="text")
   // @Lob
   // @Column(name="SUMMARY", length=512)
   // @Column(columnDefinition = "TEXT")
  //  @Column(length=10000)
   // @Column(name="SUMMARY")
   // @Lob
    String getSummary();
    Instant getCreated();
    Instant getStart();
    Instant getEnd();
    Integer getNumberInternalAttendees();
    List<PersonDto> getParticipants();
    Integer getTotalSeconds();
}
