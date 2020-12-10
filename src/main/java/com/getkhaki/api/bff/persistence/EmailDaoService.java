package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.repositories.DomainRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.EmailRepositoryInterface;
import com.sun.istack.NotNull;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class EmailDaoService {
    private final EmailRepositoryInterface emailRepository;
    private final DomainRepositoryInterface domainRepository;


    public EmailDaoService(EmailRepositoryInterface emailRepository, DomainRepositoryInterface domainRepository) {
        this.emailRepository = emailRepository;
        this.domainRepository = domainRepository;
    }

    public EmailDao upsert(@NotNull EmailDao email) {
        val domain = email.getDomain();
        if (domain == null) {
            throw new RuntimeException("domain required to upsert EmailDao");
        }

        val savedDomain = domainRepository
                .findDistinctByName(domain.getName())
                .orElseGet(() -> domainRepository.save(domain));

        email.setDomain(savedDomain);
        val emailOp = emailRepository.findDistinctByUserAndDomain_Id(email.getUser(), savedDomain.getId());
        emailOp.ifPresent(emailDao -> email.setId(emailDao.getId()));
        return emailRepository.save(email);
    }
}
