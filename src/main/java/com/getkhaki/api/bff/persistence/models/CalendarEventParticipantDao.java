package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"calendar_event_id", "email_id"}),
                @UniqueConstraint(columnNames = {"calendar_event_id", "organizer"}),
        }
)
@Accessors(chain = true)
@Getter
@Setter
public class CalendarEventParticipantDao extends EntityBaseDao {
    @ManyToOne(optional = false)
    CalendarEventDao calendarEvent;

    @ManyToOne(optional = false)
    EmailDao email;

    @Column(nullable = true)
    Boolean organizer = null;

    public CalendarEventParticipantDao setOrganizer(Boolean organizer) {
        this.organizer = organizer;
        if (organizer.equals(false)) {
            this.organizer = null;
        }
        return this;
    }
}
