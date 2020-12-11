package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface EmployeeRepositoryInterface extends JpaRepository<EmployeeDao, UUID> {
    Set<EmployeeDao> findDistinctByDepartment_OrganizationId(UUID organizationId);

    Optional<EmployeeDao> findDistinctByPerson(PersonDao personDao);
}
