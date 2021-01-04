package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepositoryInterface extends JpaRepository<EmployeeDao, UUID> {
    Page<EmployeeDao> findDistinctByDepartment_OrganizationId(UUID organizationId, Pageable pageable);

    Optional<EmployeeDao> findDistinctByPerson(PersonDao personDao);
}
