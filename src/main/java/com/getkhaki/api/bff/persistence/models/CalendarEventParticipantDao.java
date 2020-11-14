package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class CalendarEventParticipantDao extends EntityBaseDao {
    @ManyToOne
    CalendarEventDao calendarEvent;

    @ManyToOne
    EmailDao email;

    @ManyToOne
    ParticipantTypeDao participantType;
}
