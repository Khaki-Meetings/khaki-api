package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.domain.persistence.PersonPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import com.getkhaki.api.bff.persistence.repositories.PersonRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonPersistenceService implements PersonPersistenceInterface {
    private final PersonRepositoryInterface personRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public PersonPersistenceService(PersonRepositoryInterface personRepository, ModelMapper modelMapper) {
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public PersonDm getPerson(String email) {
        String[] emailParts = email.split("@");
        assert emailParts.length == 2 : "Email could not be split";

        return this.modelMapper.map(
                this.personRepository.findDistinctByEmailsUserAndEmailsDomainName(emailParts[0], emailParts[1]),
                PersonDm.class
        );
    }

    @Override
    public PersonDm updatePerson(PersonDm personDm) {
        return this.modelMapper.map(
                this.personRepository.save(
                        this.modelMapper.map(personDm, PersonDao.class)
                ), PersonDm.class
        );
    }
}
