package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.domain.persistence.PersonPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import com.getkhaki.api.bff.persistence.repositories.PersonRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

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

        PersonDao result = this.personRepository.findDistinctByEmailsUserAndEmailsDomainName(emailParts[0], emailParts[1]);
        assert result != null : String.format("Person with email of %s not found", email);

        return this.modelMapper.map(result, PersonDm.class);
    }

    @Override
    public PersonDm updatePerson(PersonDm personDm) {
        return this.modelMapper.map(
                this.personRepository.save(
                        this.modelMapper.map(personDm, PersonDao.class)
                ), PersonDm.class
        );
    }

    @Override
    public Set<PersonDm> getPersonsByCalendarEvent(String calendarEventId) {
        return this.personRepository.findDistinctByCalendarEvent(calendarEventId)
            .stream()
            .map(person -> modelMapper.map(person, PersonDm.class))
            .collect(Collectors.toSet());
    }

    @Override
    public PersonDm getOrganizerByCalendarEvent(String calendarEventId) {
        PersonDao personDao = this.personRepository.findOrganizerByCalendarEvent(calendarEventId);
        if (personDao != null) {
            return this.modelMapper.map(personDao, PersonDm.class);
        }
        return null;
    }
}
