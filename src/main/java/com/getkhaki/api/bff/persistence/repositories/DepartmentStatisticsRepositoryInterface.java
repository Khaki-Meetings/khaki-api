package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import com.getkhaki.api.bff.persistence.models.views.DepartmentStatisticsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface DepartmentStatisticsRepositoryInterface extends JpaRepository<DepartmentDao, UUID> {
    //    @Query(
//            "select" +
//                    "   department.id as departmentId," +
//                    "   department.name as departmentName," +
//                    "   (" +
//                    "       select count(employee.person.id)" +
//                    "       from employees employee " +
//                    "       inner join employee.person person" +
//                    "       inner join " +
//                    "   ) as count " +
//                    "from DepartmentDao as department" +
//                    "   inner join department.employees as employees"
//    )
    @Query(
            "select " +
                    "department.id as departmentId," +
                    "department.name as departmentName," +
                    "sum(" +
                    "   timestampdiff(" +
                    "       hour," +
                    "       calendarEvent.start," +
                    "       calendarEvent.end" +
                    "   )" +
                    ") as totalHours " +
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
    List<DepartmentStatisticsView> findAllDepartmentStatisticsInRange(
            Instant sDate,
            Instant eDate,
            UUID tenantId
    );
}
