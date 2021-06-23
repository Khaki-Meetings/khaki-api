package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import com.getkhaki.api.bff.persistence.models.views.EmployeeWithStatisticsView;
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

    @Query(
            value = "select employee as employee, " +
                    " ( select count(cepd.id) " +
                    "       from CalendarEventParticipantDao cepd " +
                    "       inner join cepd.email as emails " +
                    "       inner join emails.people as persons " +
                    "       inner join persons.employee as emp " +
                    "       where emp = employee " +
                     "         and cepd.calendarEvent.start between :sDate and :eDate " +
                    "   ) as totalMeetings," +
                    " ( select sum(timestampdiff(second, cepd.calendarEvent.start, cepd.calendarEvent.end)) " +
                    "       from CalendarEventParticipantDao cepd " +
                    "       inner join cepd.email as emails " +
                    "       inner join emails.people as persons " +
                    "       inner join persons.employee as emp " +
                    "       where emp = employee " +
                    "         and cepd.calendarEvent.start between :sDate and :eDate " +
                    "   ) as totalSeconds " +
                    " from EmployeeDao employee" +
                    "   inner join employee.person as person " +
                    "   inner join employee.department as departments " +
                    "   inner join departments.organization as org " +
                    " where org.id = :organizationId",
            countQuery = "select count(distinct employee)" +
                    " from EmployeeDao employee" +
                    "   inner join employee.person as person " +
                    "   inner join employee.department as departments " +
                    "   inner join departments.organization as org " +
                    " where org.id = :organizationId " +
                    " and (:sDate <= :eDate or :sDate >= :eDate) "
    )
    Page<EmployeeWithStatisticsView> findEmployeesWithStatistics(
            UUID organizationId, Instant sDate, Instant eDate, Pageable pageable);

    @Query(
            value = "select employee as employee, " +
                    " ( select count(cepd.id) " +
                    "       from CalendarEventParticipantDao cepd " +
                    "       inner join cepd.email as emails " +
                    "       inner join emails.people as persons " +
                    "       inner join persons.employee as emp " +
                    "       where emp = employee " +
                    "         and cepd.calendarEvent.start between :sDate and :eDate " +
                    "   ) as totalMeetings," +
                    " ( select sum(timestampdiff(second, cepd.calendarEvent.start, cepd.calendarEvent.end)) " +
                    "       from CalendarEventParticipantDao cepd " +
                    "       inner join cepd.email as emails " +
                    "       inner join emails.people as persons " +
                    "       inner join persons.employee as emp " +
                    "       where emp = employee " +
                    "         and cepd.calendarEvent.start between :sDate and :eDate " +
                    "   ) as totalSeconds " +
                    " from EmployeeDao employee" +
                    "   inner join employee.person as person " +
                    "   inner join employee.department as departments " +
                    "   inner join departments.organization as org " +
                    " where org.id = :organizationId " +
                    "   and departments.name = :department ",
            countQuery = "select count(distinct employee)" +
                    " from EmployeeDao employee" +
                    "   inner join employee.person as person " +
                    "   inner join employee.department as departments " +
                    "   inner join departments.organization as org " +
                    " where org.id = :organizationId " +
                    " and (:sDate <= :eDate or :sDate >= :eDate) " +
                    "   and departments.name = :department "
    )
    Page<EmployeeWithStatisticsView> findEmployeesWithStatistics(
            UUID organizationId, Instant sDate, Instant eDate,
            String department, Pageable pageable);

}
