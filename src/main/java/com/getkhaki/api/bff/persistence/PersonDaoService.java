package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import com.getkhaki.api.bff.persistence.repositories.PersonRepositoryInterface;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class PersonDaoService {
    private final EmailDaoService emailDaoService;
    private final PersonRepositoryInterface personRepository;

    PersonDaoService(EmailDaoService emailDaoService, PersonRepositoryInterface personRepository) {
        this.emailDaoService = emailDaoService;
        this.personRepository = personRepository;
    }

    PersonDao upsert(PersonDao person) {
        EmailDao email = person.getPrimaryEmail();
        if (email == null) {
            throw new RuntimeException("Email required to upsert PersonDao");
        }

        val savedEmail = emailDaoService.upsert(email);

        val personOp = personRepository.findDistinctByEmails(savedEmail);
        personOp.ifPresentOrElse(
                personDao -> {
                    person.setId(personDao.getId());
                    person.setEmails(personDao.getEmails());
                    person.setEmployee(personDao.getEmployee());
                },
                () -> {
                    savedEmail.setPerson(person);
                    person.setEmails(List.of(savedEmail));
                }
        );

        return personRepository.save(person);
    }
}
