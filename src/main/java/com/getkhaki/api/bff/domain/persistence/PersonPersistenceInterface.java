package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.PersonDm;
import java.util.Set;

public interface PersonPersistenceInterface {
    PersonDm getPerson(String email);

    PersonDm updatePerson(PersonDm personDm);

    Set<PersonDm> getPersonsByCalendarEvent(String calendarEventId);
}
