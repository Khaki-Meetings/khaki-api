package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.PersonDm;

public interface PersonPersistenceInterface {
    PersonDm getPerson(String email);

    PersonDm updatePerson(PersonDm personDm);
}
