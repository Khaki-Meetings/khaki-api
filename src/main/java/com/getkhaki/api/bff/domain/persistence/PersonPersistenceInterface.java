package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.PersonDm;
import java.util.Set;
import java.util.UUID;

public interface PersonPersistenceInterface {
    PersonDm getPerson(String email);

    PersonDm getPersonById(UUID id);

    PersonDm updatePerson(PersonDm personDm);

    Set<PersonDm> getPersonsByCalendarEvent(UUID calendarEventId);

    PersonDm getOrganizerByCalendarEvent(UUID calendarEventId);
}
