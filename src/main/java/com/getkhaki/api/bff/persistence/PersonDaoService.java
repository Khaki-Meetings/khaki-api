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

    PersonDao upsert(PersonDao personDao) {
        EmailDao email = personDao.getPrimaryEmail();
        if (email == null) {
            throw new RuntimeException("Email required to upsert PersonDao");
        }

        val savedEmail = emailDaoService.upsert(email);
        personDao.setEmails(List.of(savedEmail));

        val personOp = personRepository.findDistinctByEmails(savedEmail);
        personOp.ifPresent(emailDao -> personDao.setId(personDao.getId()));

        return personRepository.save(personDao);
    }
}
