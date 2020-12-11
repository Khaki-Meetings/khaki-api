package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailRepositoryInterface extends JpaRepository<EmailDao, UUID> {
    Optional<EmailDao> findDistinctByUserAndDomain_Id(String user, UUID domainId);
}
