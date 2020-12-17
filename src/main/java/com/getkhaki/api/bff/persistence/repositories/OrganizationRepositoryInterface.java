package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.FlagDao;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface OrganizationRepositoryInterface extends JpaRepository<OrganizationDao, UUID> {
    Set<OrganizationDao> findDistinctByFlagsNameNotContaining(String flagName);
}
