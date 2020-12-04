package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.domain.persistence.PersonPersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonPersistenceInterface personPersistenceService;

    @Autowired
    public PersonService(PersonPersistenceInterface personPersistenceService) {
        this.personPersistenceService = personPersistenceService;
    }

    public PersonDm getPerson(String email) {
        return this.personPersistenceService.getPerson(email);
    }

    public PersonDm updatePerson(PersonDm personDm) {
        return this.personPersistenceService.updatePerson(personDm);
    }
}
