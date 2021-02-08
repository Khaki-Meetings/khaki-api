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
                    "department.id as departmentId," +
                    "department.name as departmentName," +
                    "sum(" +
                    "   timestampdiff(" +
                    "       second," +
                    "       calendarEvent.start," +
                    "       calendarEvent.end" +
                    "   )" +
                    ") as totalSeconds, " +
                    "(select count(*) from EmployeeDao employee " +
                    "   where employee.department = department) as numberEmployees " +
                    "from DepartmentDao department " +
                    "   inner join department.organization organization" +
                    "   inner join department.employees employees" +
                    "   inner join employees.person person" +
                    "   inner join person.emails emails" +
                    "   inner join CalendarEventParticipantDao calendarEventParticipant" +
                    "       on calendarEventParticipant.email.id = emails.id" +
                    "   inner join calendarEventParticipant.calendarEvent calendarEvent " +
                    "where organization.id = :tenantId " +
                    "   and calendarEvent.start between :sDate and :eDate " +
                    "group by department.id"
    )
    List<DepartmentStatisticsView> findExternalDepartmentStatisticsInRange(
            Instant sDate,
            Instant eDate,
            UUID tenantId
    );

    @Query(
            "select " +
                    "department.id as departmentId," +
                    "department.name as departmentName," +
                    "sum(" +
                    "   timestampdiff(" +
                    "       second," +
                    "       calendarEvent.start," +
                    "       calendarEvent.end" +
                    "   )" +
                    ") as totalSeconds, " +
                    "(select count(*) from EmployeeDao employee " +
                    "   where employee.department = department) as numberEmployees " +
                    "from DepartmentDao department " +
                    "   inner join department.organization organization" +
                    "   inner join department.employees employees" +
                    "   inner join employees.person person" +
                    "   inner join person.emails emails" +
                    "   inner join CalendarEventParticipantDao calendarEventParticipant" +
                    "       on calendarEventParticipant.email.id = emails.id" +
                    "   inner join calendarEventParticipant.calendarEvent calendarEvent " +
                    "where organization.id = :tenantId " +
                    "   and calendarEvent.start between :sDate and :eDate " +
                    "           and exists (" +
                    "               select count(distinct domain.name)" +
                    "               from CalendarEventDao innerCalendarEvent" +
                    "               inner join innerCalendarEvent.participants innerParticipants" +
                    "               inner join innerParticipants.email.domain domain" +
                    "               where innerCalendarEvent = calendarEvent" +
                    "               group by innerCalendarEvent" +
                    "               having count(distinct domain.name) = 1" +
                    "           )" +
                    "group by department.id"
    )
    List<DepartmentStatisticsView> findInternalDepartmentStatisticsInRange(
            Instant sDate,
            Instant eDate,
            UUID tenantId
    );
}
