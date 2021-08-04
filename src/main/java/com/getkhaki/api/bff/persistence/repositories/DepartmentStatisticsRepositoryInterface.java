package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import com.getkhaki.api.bff.persistence.models.views.DepartmentStatisticsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface DepartmentStatisticsRepositoryInterface extends JpaRepository<DepartmentDao, UUID> {
    @Query(
            "select " +
            "   department.id as departmentId, " +
            "   department.name as departmentName, " +
            "   coalesce(sum(timestampdiff(second, calendarEvent.start, calendarEvent.end)), 0) as totalSeconds, " +
            "   coalesce(( " +
            "       SELECT count(*) * 3600 * 8 * (" +
            "           5 * (timestampdiff(day, :sDate, :eDate) / 7) " +
            "           + SUBSTRING('0123444401233334012222340111123400001234000123440', " +
            "           7 * dayofweek(:sDate) + dayofweek(:eDate) + 1, 1)" +
            "       ) " +
            "       from EmployeeDao employee " +
            "       where employee.department = department " +
            "   ), 0) as inventorySecondsAvailable " +
            "from DepartmentDao department " +
            "inner join department.organization organization " +
            "inner join department.employees employees " +
            "inner join employees.person person " +
            "inner join person.emails emails " +
            "inner join CalendarEventParticipantDao calendarEventParticipant " +
            "   on calendarEventParticipant.email.id = emails.id " +
            "inner join calendarEventParticipant.calendarEvent calendarEvent " +
            "where organization.id = :tenantId " +
            "and calendarEvent.start between :sDate and :eDate " +
            "group by department.id"
    )
    List<DepartmentStatisticsView> findExternalDepartmentStatisticsInRange(
            Instant sDate,
            Instant eDate,
            UUID tenantId
    );

    @Query(
            "select " +
            "   department.id as departmentId, " +
            "   department.name as departmentName, " +
            "   coalesce((" +
            "     select sum(timestampdiff(second, calendarEvent.start, calendarEvent.end)) " +
            "       from CalendarEventDao calendarEvent" +
            "       inner join calendarEvent.participants cap_count" +
            "       inner join cap_count.email as email_count" +
            "       inner join email_count.domain as domain_count" +
            "       inner join domain_count.organizations as org_count" +
            "       inner join email_count.people as persons " +
            "       inner join persons.employee as employee " +
            "       inner join employee.department as departments " +
            "     where org_count.id = :tenantId" +
            "       and departments.id = department.id " +
            "       and calendarEvent.start between :sDate and :eDate " +
            "       and exists ( " +
            "           select count(distinct domain.name) " +
            "           from CalendarEventDao innerCalendarEvent " +
            "           inner join innerCalendarEvent.participants innerParticipants " +
            "           inner join innerParticipants.email.domain domain " +
            "           where innerCalendarEvent = calendarEvent " +
            "           group by innerCalendarEvent " +
            "           having count(distinct domain.name) = 1 " +
            "       )" +
            "   ), 0) as totalSeconds, " +
            "   coalesce(( " +
            "       SELECT count(*) * 3600 * 8 * (" +
            "           5 * (timestampdiff(day, :sDate, :eDate) / 7) " +
            "           + SUBSTRING('0123444401233334012222340111123400001234000123440', " +
            "           7 * dayofweek(:sDate) + dayofweek(:eDate) + 1, 1) " +
            "       ) " +
            "       from EmployeeDao employee " +
            "       where employee.department = department" +
            "   ), 0) as inventorySecondsAvailable " +
            "from DepartmentDao department " +
            "inner join department.organization organization " +
            "where organization.id = :tenantId " +
            "group by department.id"
    )
    List<DepartmentStatisticsView> findInternalDepartmentStatisticsInRange(
            Instant sDate,
            Instant eDate,
            UUID tenantId
    );
}
