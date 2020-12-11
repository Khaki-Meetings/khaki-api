package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
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
    Instant created;

    @Column(nullable = false)
    Instant start;

    Instant end;

    @OneToMany(mappedBy = "calendarEvent")
    List<CalendarEventParticipantDao> participants = new ArrayList<>();
}
