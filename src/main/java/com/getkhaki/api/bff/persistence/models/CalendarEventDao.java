package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class CalendarEventDao extends EntityBaseDao {
    @Column(unique = true)
    String googleCalendarId;

    @Column(nullable = false, length = 65535, columnDefinition = "TEXT")
    String summary;

    @Column(nullable = false)
    Instant created;

    @Column(nullable = false)
    Instant start;

    Instant end;

    @OneToMany(mappedBy = "calendarEvent")
    List<CalendarEventParticipantDao> participants = new ArrayList<>();

    String description;

    String recurringEventId;

    String visibility;

    Integer attachmentCount;
}
