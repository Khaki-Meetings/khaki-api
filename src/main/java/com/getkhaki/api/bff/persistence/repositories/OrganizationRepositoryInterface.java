package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationRepositoryInterface extends JpaRepository<OrganizationDao, UUID> {
    Optional<OrganizationDao> findDistinctByName(String name);

    Optional<OrganizationDao> findById(UUID id);
}
