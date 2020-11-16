package com.getkhaki.api.bff.persistence.models;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.Column;
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

    @Column(nullable = false)
    String summary;

    @Column(nullable = false)
    ZonedDateTime created;

    @Column(nullable = false)
    ZonedDateTime start;

    ZonedDateTime end;

    @OneToMany(mappedBy = "calendarEvent")
    List<CalendarEventParticipantDao> participants;
}
