package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface DepartmentRepositoryInterface extends JpaRepository<DepartmentDao, UUID> {
    Page<DepartmentDao> findDistinctByOrganizationId(UUID organizationId, Pageable pageable);
    Optional<DepartmentDao> findDistinctByNameAndOrganization(String name, OrganizationDao organizationDao);
}
