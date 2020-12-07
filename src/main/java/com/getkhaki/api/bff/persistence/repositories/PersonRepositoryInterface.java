package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.PersonDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonRepositoryInterface extends JpaRepository<PersonDao, UUID> {
    PersonDao findDistinctByEmailsUserAndEmailsDomainName(String userName, String domainName);
}
