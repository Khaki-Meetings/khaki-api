package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.domain.persistence.PersonPersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonPersistenceInterface personPersistenceInterface;

    @Autowired
    public PersonService(PersonPersistenceInterface personPersistenceInterface) {
        this.personPersistenceInterface = personPersistenceInterface;
    }

    public PersonDm getPerson(String email) {
        return this.personPersistenceInterface.getPerson(email);
    }

    public PersonDm updatePerson(PersonDm personDm) {
        return this.personPersistenceInterface.updatePerson(personDm);
    }
}
