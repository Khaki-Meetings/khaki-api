package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class CalendarEventDao extends EntityBaseDao {
    String googleCalendarId;

    String summary;

    ZonedDateTime created;

    ZonedDateTime start;

    ZonedDateTime end;

    @OneToMany(mappedBy = "calendarEvent")
    List<CalendarEventParticipantDao> participants;
}
