package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Accessors(chain = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"calendar_event_id", "email_id"}))
@EqualsAndHashCode(callSuper = true)
public class CalendarEventParticipantDao extends EntityBaseDao {
    @ManyToOne(optional = false)
    CalendarEventDao calendarEvent;

    @ManyToOne(optional = false)
    EmailDao email;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    ParticipantTypeDao participantType;
}
