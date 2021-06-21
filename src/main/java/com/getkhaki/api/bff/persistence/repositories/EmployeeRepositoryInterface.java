package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsAggregateView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepositoryInterface extends JpaRepository<EmployeeDao, UUID> {
    Page<EmployeeDao> findDistinctByDepartment_OrganizationId(UUID organizationId, Pageable pageable);

    Optional<EmployeeDao> findDistinctByPerson(PersonDao personDao);

    @Query(
            value = "select employee " +
                    " from EmployeeDao employee" +
                    "   inner join employee.person as person " +
                    "   inner join employee.department as departments " +
                    "   inner join departments.organization as org " +
                    " where org.id = :organizationId" +
                    "   and departments.name = :department ",
            countQuery = "select count(distinct employee)" +
                    " from EmployeeDao employee" +
                    "   inner join employee.person as person " +
                    "   inner join employee.department as departments " +
                    "   inner join departments.organization as org " +
                    " where org.id = :organizationId" +
                    "   and departments.name = :department "
    )
    Page<EmployeeDao> findEmployeesByDepartment(UUID organizationId, String department, Pageable pageable);

}
