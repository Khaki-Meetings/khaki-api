package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.TimeBlockSummaryDao;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsEmployeeTimeView;
import com.getkhaki.api.bff.persistence.models.views.CalendarMeetingEfficacyView;
import com.getkhaki.api.bff.persistence.models.views.TimeBlockSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface TimeBlockSummaryRepositoryInterface extends JpaRepository<TimeBlockSummaryDao, UUID> {

    @Query(
            value = "select t from TimeBlockSummaryDao t " +
                    " where t.organizationId = :organizationId " +
                    " and t.start = :sStartDate " +
                    " and t.end = :sEndDate " +
                    " and t.filter = :filter "
    )
    Optional<TimeBlockSummaryDao> findDistinctByOrganizationAndStartAndFilter(UUID organizationId,
                                                                              Instant sStartDate, Instant sEndDate, String filter);

    String numOfWorkdays = "(5 * (timestampdiff(day, :sDate, :eDate) / 7) " +
            "  + SUBSTRING('0123444401233334012222340111123400001234000123440', " +
            "  7 * (dayofweek(:sDate) - 1) + dayofweek(:eDate), 1) " +
            " ) ";

    @Query(
            value = "select " + numOfWorkdays + " from dual",
            nativeQuery = true)
    Integer findNumberOfWorkdaysBetweenDates(Instant sDate, Instant eDate);

    @Query(
            "select " +
                    "sum(" +
                    "   timestampdiff(second, calendarEvent.start, calendarEvent.end)" +
                    "*" +
                    " (" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "       inner join cap_count.email as email_count" +
                    "       inner join email_count.domain as domain_count" +
                    "       inner join domain_count.organizations as org_count" +
                    "       inner join email_count.people as persons " +
                    "       inner join persons.employee as employee " +
                    "       inner join employee.department as departments " +
                    "     where cap_count.calendarEvent = calendarEvent " +
                    "       and org_count.id = :tenantId" +
                    "       and departments.name = :departmentName " +
                    "     )" +
                    ") as totalSeconds, " +
                    "sum(" +
                    "   timestampdiff(second, calendarEvent.start, calendarEvent.end)" +
                    ") as meetingLengthSeconds, " +
                    " sum((" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "       inner join cap_count.email as email_count" +
                    "       inner join email_count.domain as domain_count" +
                    "       inner join domain_count.organizations as org_count" +
                    "       inner join email_count.people as persons " +
                    "       inner join persons.employee as employee " +
                    "       inner join employee.department as departments " +
                    "     where cap_count.calendarEvent = calendarEvent " +
                    "       and org_count.id = :tenantId" +
                    "       and departments.name = :departmentName " +
                    ") " +
                    ") as totalInternalMeetingAttendees, " +
                    " sum(" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "     where cap_count.calendarEvent = calendarEvent " +
                    ") as totalMeetingAttendees, " +
                    " ( select count(*) " +
                    "   from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   where organization.id = :tenantId " +
                    "     and departments.name = :departmentName " +
                    ") as numEmployees, " +
                    "count(distinct calendarEventParticipant.calendarEvent) as meetingCount " +
                    "from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   inner join employees.person as person " +
                    "   inner join person.emails as emails " +
                    "   inner join CalendarEventParticipantDao calendarEventParticipant " +
                    "       on calendarEventParticipant.email = emails.id " +
                    "       and departments.name = :departmentName " +
                    "   inner join calendarEventParticipant.calendarEvent as calendarEvent " +
                    "where organization.id = :tenantId " +
                    "   and departments.name = :departmentName " +
                    "   and calendarEvent.start between :sDate and :eDate" +
                    "   and exists (" +
                    "       select count(distinct domain.name)" +
                    "       from CalendarEventDao innerCalendarEvent" +
                    "       inner join innerCalendarEvent.participants innerParticipants" +
                    "       inner join innerParticipants.email.domain domain" +
                    "       where innerCalendarEvent = calendarEvent" +
                    "       group by innerCalendarEvent" +
                    "       having count(distinct domain.name) > 1" +
                    "   )"
    )
    TimeBlockSummaryView findDepartmentExternalTimeBlockSummaryInRange(Instant sDate, Instant eDate, String departmentName, UUID tenantId);

    String externalTimeBlockSummaryInRangeFromClause =
            "from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   inner join employees.person as person " +
                    "   inner join person.emails as emails " +
                    "   inner join CalendarEventParticipantDao calendarEventParticipant " +
                    "       on calendarEventParticipant.email = emails.id " +
                    "   inner join calendarEventParticipant.calendarEvent as calendarEvent " +
                    "where organization.id = :tenantId " +
                    "   and calendarEvent.start between :sDate and :eDate" +
                    "   and exists (" +
                    "       select count(distinct domain.name)" +
                    "       from CalendarEventDao innerCalendarEvent" +
                    "       inner join innerCalendarEvent.participants innerParticipants" +
                    "       inner join innerParticipants.email.domain domain" +
                    "       where innerCalendarEvent = calendarEvent" +
                    "       group by innerCalendarEvent" +
                    "       having count(distinct domain.name) > 1" +
                    "   )";

    @Query(
            "select " +
                    "sum(" +
                    "   timestampdiff(second, calendarEvent.start, calendarEvent.end)" +
                    "*" +
                    " (" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "       inner join cap_count.email as email_count" +
                    "       inner join email_count.domain as domain_count" +
                    "       inner join domain_count.organizations as org_count" +
                    "       inner join email_count.people as persons " +
                    "       inner join persons.employee as employee " +
                    "     where cap_count.calendarEvent = calendarEvent " +
                    "       and org_count.id = :tenantId" +
                    "     )" +
                    ") as totalSeconds, " +
                    " ( select count(*) " +
                    "   from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   where organization.id = :tenantId " +
                    ") as numEmployees, " +
                    "count(distinct calendarEventParticipant.calendarEvent) as meetingCount " +
                    externalTimeBlockSummaryInRangeFromClause

    )
    TimeBlockSummaryView findExternalTimeBlockSummaryInRange(Instant sDate, Instant eDate, UUID tenantId);

    @Query(
            "select " +
                    "sum(" +
                    "   timestampdiff(second, ced.start, ced.end)" +
                    ") as meetingLengthSeconds, " +
                    " sum((" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "       inner join cap_count.email as email_count" +
                    "       inner join email_count.domain as domain_count" +
                    "       inner join domain_count.organizations as org_count" +
                    "     where cap_count.calendarEvent.id = ced.id " +
                    "       and org_count.id = :tenantId" +
                    ") " +
                    ") as totalInternalMeetingAttendees, " +
                    " sum(" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "     where cap_count.calendarEvent.id = ced.id " +
                    ") as totalMeetingAttendees " +

                    " from CalendarEventDao ced" +
                    " where ced.id in (" +
                    " select distinct(calendarEvent.id)  " +
                    externalTimeBlockSummaryInRangeFromClause +
                    " ) "
    )
    TimeBlockSummaryView findExternalTimeBlockAggregateSummaryInRange(Instant sDate, Instant eDate, UUID tenantId);

    String departmentExternalTimeBlockSummaryInRangeFromClause =
            "from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   inner join employees.person as person " +
                    "   inner join person.emails as emails " +
                    "   inner join CalendarEventParticipantDao calendarEventParticipant " +
                    "       on calendarEventParticipant.email = emails.id " +
                    "   inner join calendarEventParticipant.calendarEvent as calendarEvent " +
                    "where organization.id = :tenantId " +
                    "   and departments.name = :departmentName " +
                    "   and calendarEvent.start between :sDate and :eDate" +
                    "   and exists (" +
                    "       select count(distinct domain.name)" +
                    "       from CalendarEventDao innerCalendarEvent" +
                    "       inner join innerCalendarEvent.participants innerParticipants" +
                    "       inner join innerParticipants.email.domain domain" +
                    "       where innerCalendarEvent = calendarEvent" +
                    "       group by innerCalendarEvent" +
                    "       having count(distinct domain.name) > 1" +
                    "   )";

    @Query(
            "select " +
                    " sum(" +
                    "   timestampdiff(second, ced.start, ced.end)" +
                    ") as meetingLengthSeconds, " +
                    " sum(" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "     where cap_count.calendarEvent.id = ced.id " +
                    ") as totalMeetingAttendees, " +
                    " sum((" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "       inner join cap_count.email as email_count" +
                    "       inner join email_count.domain as domain_count" +
                    "       inner join domain_count.organizations as org_count" +
                    "       inner join email_count.people as persons " +
                    "       inner join persons.employee as employee " +
                    "       inner join employee.department as departments " +
                    "     where cap_count.calendarEvent.id = ced.id " +
                    "       and org_count.id = :tenantId" +
                    "       and departments.name = :departmentName " +
                    ") " +
                    ") as totalInternalMeetingAttendees " +
                    " from CalendarEventDao ced" +
                    " where ced.id in (" +
                    " select distinct(calendarEvent.id)  " +
                    departmentExternalTimeBlockSummaryInRangeFromClause +
                    " ) "
    )
    TimeBlockSummaryView findDepartmentExternalTimeBlockAggregateSummaryInRange(Instant sDate, Instant eDate, String departmentName, UUID tenantId);


    String departmentInternalTimeBlockSummaryInRangeFromClause =
            "from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   inner join employees.person as person " +
                    "   inner join person.emails as emails " +
                    "   inner join CalendarEventParticipantDao calendarEventParticipant " +
                    "       on calendarEventParticipant.email = emails.id " +
                    "   inner join calendarEventParticipant.calendarEvent as calendarEvent " +
                    "where organization.id = :tenantId " +
                    "   and departments.name = :departmentName " +
                    "   and calendarEvent.start between :sDate and :eDate" +
                    "   and exists (" +
                    "       select count(distinct domain.name)" +
                    "       from CalendarEventDao innerCalendarEvent" +
                    "       inner join innerCalendarEvent.participants innerParticipants" +
                    "       inner join innerParticipants.email.domain domain" +
                    "       where innerCalendarEvent = calendarEvent" +
                    "       group by innerCalendarEvent" +
                    "       having count(distinct domain.name) = 1" +
                    "   )";

    @Query(
            "select " +
                    "sum(" +
                    "   timestampdiff(second, calendarEvent.start, calendarEvent.end)" +
                    "*" +
                    " (" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "       inner join cap_count.email as email_count" +
                    "       inner join email_count.domain as domain_count" +
                    "       inner join domain_count.organizations as org_count" +
                    "       inner join email_count.people as persons " +
                    "       inner join persons.employee as employee " +
                    "       inner join employee.department as departments " +
                    "     where cap_count.calendarEvent = calendarEvent " +
                    "       and org_count.id = :tenantId" +
                    "       and departments.name = :departmentName " +
                    "     )" +
                    ") as totalSeconds, " +
                    " ( select count(*) " +
                    "   from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   where organization.id = :tenantId " +
                    "   and departments.name = :departmentName " +
                    ") as numEmployees, " +
                    "count(distinct calendarEventParticipant.calendarEvent) as meetingCount " +
                    departmentInternalTimeBlockSummaryInRangeFromClause
    )
    TimeBlockSummaryView findDepartmentInternalTimeBlockSummaryInRange(Instant sDate, Instant eDate, String departmentName, UUID tenantId);

    @Query(
            "select " +
                    " sum(" +
                    "   timestampdiff(second, ced.start, ced.end)" +
                    ") as meetingLengthSeconds, " +
                    " sum(" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "     where cap_count.calendarEvent.id = ced.id " +
                    ") as totalMeetingAttendees, " +
                    " sum((" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "       inner join cap_count.email as email_count" +
                    "       inner join email_count.domain as domain_count" +
                    "       inner join domain_count.organizations as org_count" +
                    "       inner join email_count.people as persons " +
                    "       inner join persons.employee as employee " +
                    "       inner join employee.department as departments " +
                    "     where cap_count.calendarEvent.id = ced.id " +
                    "       and org_count.id = :tenantId" +
                    "       and departments.name = :departmentName " +
                    ") " +
                    ") as totalInternalMeetingAttendees " +
                    " from CalendarEventDao ced" +
                    " where ced.id in (" +
                    " select distinct(calendarEvent.id)  " +
                    departmentInternalTimeBlockSummaryInRangeFromClause +
                    " ) "
    )
    TimeBlockSummaryView findDepartmentInternalTimeBlockAggregateSummaryInRange(Instant sDate, Instant eDate, String departmentName, UUID tenantId);


    String internalTimeBlockSummaryInRangeFromClause = " from OrganizationDao organization " +
            "   inner join organization.departments as departments " +
            "   inner join departments.employees as employees " +
            "   inner join employees.person as person " +
            "   inner join person.emails as emails " +
            "   inner join CalendarEventParticipantDao calendarEventParticipant " +
            "       on calendarEventParticipant.email = emails.id " +
            "   inner join calendarEventParticipant.calendarEvent as calendarEvent " +
            "where organization.id = :tenantId " +
            "   and calendarEvent.start between :sDate and :eDate" +
            "   and exists (" +
            "       select count(distinct domain.name)" +
            "       from CalendarEventDao innerCalendarEvent" +
            "       inner join innerCalendarEvent.participants innerParticipants" +
            "       inner join innerParticipants.email.domain domain" +
            "       where innerCalendarEvent = calendarEvent" +
            "       group by innerCalendarEvent" +
            "       having count(distinct domain.name) = 1" +
            "   )";

    @Query(
            "select " +
                    "sum(" +
                    "   timestampdiff(second, calendarEvent.start, calendarEvent.end)" +
                    "*" +
                    " (" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "       inner join cap_count.email as email_count" +
                    "       inner join email_count.domain as domain_count" +
                    "       inner join domain_count.organizations as org_count" +
                    "     where cap_count.calendarEvent = calendarEvent " +
                    "       and org_count.id = :tenantId" +
                    "     )" +
                    ") as totalSeconds, " +
                    " ( select count(*) " +
                    "   from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   where organization.id = :tenantId ) as numEmployees, " +
                    "count(distinct calendarEventParticipant.calendarEvent) as meetingCount " +
                    internalTimeBlockSummaryInRangeFromClause
    )
    TimeBlockSummaryView findInternalTimeBlockSummaryInRange(Instant sDate, Instant eDate, UUID tenantId);

    @Query(
            "select " +
                    " sum(timestampdiff(second, ced.start, ced.end)) as meetingLengthSeconds, " +
                    " sum(" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "       inner join cap_count.email as email_count" +
                    "       inner join email_count.domain as domain_count" +
                    "       inner join domain_count.organizations as org_count" +
                    "     where cap_count.calendarEvent.id = ced.id " +
                    "       and org_count.id = :tenantId" +
                    " " +
                    ") as totalInternalMeetingAttendees, " +
                    " sum(" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "     where cap_count.calendarEvent.id = ced.id " +
                    ") as totalMeetingAttendees " +

                    " from CalendarEventDao ced" +
                    " where ced.id in (" +
                    " select distinct(calendarEvent.id)  " +
                    internalTimeBlockSummaryInRangeFromClause +
                    " ) "
    )
    TimeBlockSummaryView findInternalTimeBlockAggregateSummaryInRange(Instant sDate, Instant eDate, UUID tenantId);


    @Query(
            "select people.id as personId, email.user as firstName," +
                    "   count(calendarEvent) as meetingCount, " +
                    "   sum(timestampdiff(second, calendarEvent.start, calendarEvent.end)) as totalSeconds " +
                    "from CalendarEventDao as calendarEvent " +
                    "   inner join calendarEvent.participants as participants " +
                    "   inner join participants.email as email " +
                    "   inner join email.domain.organizations as organization " +
                    "   inner join email.people as people " +
                    "   inner join people.employee as employee " +
                    "where employee.id = :employeeId " +
                    "   and  organization.id = :tenantId " +
                    "   and calendarEvent.start between :sDate and :eDate " +
                    "group by email"
    )
    TimeBlockSummaryView findIndividualExternalTimeBlockSummaryInRange(
            UUID employeeId, Instant sDate, Instant eDate, UUID tenantId
    );

    @Query("select people.id as personId, email.user as firstName," +
            "   count(calendarEvent) as meetingCount, " +
            "   sum(timestampdiff(second, calendarEvent.start, calendarEvent.end)) as totalSeconds " +
            "from CalendarEventDao as calendarEvent " +
            "   inner join calendarEvent.participants as participants " +
            "   inner join participants.email as email " +
            "   inner join email.domain.organizations as organization " +
            "   inner join email.people as people " +
            "   inner join people.employee as employee " +
            "where employee.id = :employeeId " +
            "   and  organization.id = :tenantId " +
            "   and calendarEvent.start between :sDate and :eDate " +
            "   and exists (" +
            "       select count(distinct domain.name)" +
            "       from CalendarEventDao innerCalendarEvent" +
            "       inner join innerCalendarEvent.participants innerParticipants" +
            "       inner join innerParticipants.email.domain domain" +
            "       where innerCalendarEvent = calendarEvent" +
            "       group by innerCalendarEvent" +
            "       having count(distinct domain.name) = 1" +
            "   ) " +
            "group by email")
    TimeBlockSummaryView findIndividualInternalTimeBlockSummaryInRange(
            UUID employeeId, Instant sDate, Instant eDate, UUID tenantId
    );

    @Query(
            value = "select t1.numOver as numOverThreshold, t2.numEmployees as numEmployees " +
                    "from ( " +
                    "   select 1 as common_key, count(*) as numOver " +
                    "   from ( " +
                    "       select COUNT(DISTINCT(pd.id)) as numOver " +
                    "       from person_dao pd, " +
                    "           email_dao_people edp2, " +
                    "           email_dao ed2, " +
                    "           calendar_event_participant_dao cepd2, " +
                    "           domain_dao_organizations ddo , " +
                    "           calendar_event_dao ced " +
                    "       where cepd2.calendar_event_id = ced.id " +
                    "           and cepd2.email_id = ed2.id " +
                    "           and ed2.domain_id = ddo.domains_id " +
                    "           and ed2.id = edp2.emails_id " +
                    "           and pd.id = edp2.people_id  " +
                    "           and ddo.organizations_id = :tenantId " +
                    "           and ced.`start` > :sDate " +
                    "           and ced.`start` < :eDate " +
                    "       group by pd.id  " +
                    "       having (sum(TIMESTAMPDIFF(minute, ced.`start`, ced.`end`)) / " +
                    "               (" + numOfWorkdays + " * 8 * 60)) * 100 > :minThreshold " +
                    "   ) c1 " +
                    ") as t1 " +
                    "JOIN ( " +
                    "   select 1 as common_key, count(DISTINCT(pd.id)) as numEmployees " +
                    "   from person_dao pd, " +
                    "       email_dao_people edp2, " +
                    "       email_dao ed2, " +
                    "       domain_dao_organizations ddo " +
                    "   where ed2.domain_id = ddo.domains_id " +
                    "       and ed2.id = edp2.emails_id " +
                    "       and pd.id = edp2.people_id  " +
                    "       and ddo.organizations_id = :tenantId " +
                    ") as t2 " +
                    "on t1.common_key = t2.common_key "
            , nativeQuery = true
    )
    CalendarEventsEmployeeTimeView getCalendarEventEmployeeTime(
            UUID tenantId, Instant sDate, Instant eDate, Integer minThreshold);

    @Query(
            value = "select t1.numOver as numOverThreshold, t2.numEmployees as numEmployees " +
                    "from ( " +
                    "   select 1 as common_key, count(*) as numOver " +
                    "   from ( " +
                    "       select COUNT(DISTINCT(pd.id)) as numOver " +
                    "       from person_dao pd, " +
                    "           email_dao_people edp2, " +
                    "           email_dao ed2, " +
                    "           calendar_event_participant_dao cepd2, " +
                    "           domain_dao_organizations ddo , " +
                    "           calendar_event_dao ced, " +
                    "           employee_dao emp, " +
                    "           department_dao dept " +
                    "       where cepd2.calendar_event_id = ced.id " +
                    "           and cepd2.email_id = ed2.id " +
                    "           and ed2.domain_id = ddo.domains_id " +
                    "           and ed2.id = edp2.emails_id " +
                    "           and pd.id = edp2.people_id  " +
                    "           and ddo.organizations_id = :tenantId " +
                    "           and pd.id = emp.person_id " +
                    "           and dept.id = emp.department_id " +
                    "           and dept.name = :departmentName " +
                    "           and ced.`start` > :sDate " +
                    "           and ced.`start` < :eDate " +
                    "       group by pd.id  " +
                    "       having (sum(TIMESTAMPDIFF(minute, ced.`start`, ced.`end`)) / " +
                    "               (" + numOfWorkdays + " * 8 * 60)) * 100 > :minThreshold " +
                    "   ) c1 " +
                    ") as t1 " +
                    "JOIN ( " +
                    "   select 1 as common_key, count(DISTINCT(pd.id)) as numEmployees " +
                    "   from person_dao pd, " +
                    "       email_dao_people edp2, " +
                    "       email_dao ed2, " +
                    "       domain_dao_organizations ddo, " +
                    "           calendar_event_dao ced, " +
                    "           employee_dao emp, " +
                    "           department_dao dept " +
                    "   where ed2.domain_id = ddo.domains_id " +
                    "       and ed2.id = edp2.emails_id " +
                    "       and pd.id = edp2.people_id  " +
                    "           and pd.id = emp.person_id " +
                    "           and dept.id = emp.department_id " +
                    "           and dept.name = :departmentName " +
                    "       and ddo.organizations_id = :tenantId " +
                    ") as t2 " +
                    "on t1.common_key = t2.common_key "
            , nativeQuery = true
    )
    CalendarEventsEmployeeTimeView getCalendarEventEmployeeTime(
            UUID tenantId, Instant sDate, Instant eDate, Integer minThreshold, String departmentName);

    @Query(
            value = " select " +
                    "   avg(totalTime) as averageStaffTimePerMeeting," +
                    "   avg(mtgLen) as averageMeetingLength " +
                    "   from ( " +
                    "       select sum(timestampdiff(second, ced.start, ced.end)) as TotalTime, " +
                    "               count(distinct pd.id) as attendees, " +
                    "               timestampdiff(second, ced.start, ced.end) as mtgLen " +
                    "       from calendar_event_dao ced, calendar_event_participant_dao cepd, " +
                    "           email_dao ed, email_dao_people edp, person_dao pd , domain_dao dd, " +
                    "           domain_dao_organizations ddo " +
                    "       WHERE ced.id = cepd.calendar_event_id " +
                    "         and ed.id = cepd.email_id " +
                    "         and ed.id = edp.emails_id " +
                    "         and edp.people_id = pd.id " +
                    "         and `start` > :sDate and `start` < :eDate " +
                    "         and dd.id = ed.domain_id " +
                    "         and dd.id = ddo.domains_id " +
                    "         and ddo.organizations_id = :tenantId " +
                    "         and exists ( " +
                    "           select count(distinct domaindao26_.name) " +
                    "           from calendar_event_dao calendarev23_ " +
                    "           inner join " +
                    "               calendar_event_participant_dao participan24_ " +
                    "                   on calendarev23_.id=participan24_.calendar_event_id " +
                    "           inner join " +
                    "               email_dao emaildao25_ " +
                    "                   on participan24_.email_id=emaildao25_.id " +
                    "           inner join " +
                    "               domain_dao domaindao26_ " +
                    "                   on emaildao25_.domain_id=domaindao26_.id " +
                    "           where calendarev23_.id = ced.id " +
                    "           group by calendarev23_.id " +
                    "           having count(distinct domaindao26_.name) = 1 " +
                    "         ) " +
                    "         group by hex(ced.id) " +
                    "   ) as x"
            , nativeQuery = true
    )
    CalendarMeetingEfficacyView getInternalMeetingEfficacyAverages(
            UUID tenantId, Instant sDate, Instant eDate);

    @Query(
            value = " select " +
                    "   avg(totalTime) as averageStaffTimePerMeeting," +
                    "   avg(mtgLen) as averageMeetingLength " +
                    "   from ( " +
                    "       select sum(timestampdiff(second, ced.start, ced.end)) as TotalTime, " +
                    "               count(distinct pd.id) as attendees, " +
                    "               timestampdiff(second, ced.start, ced.end) as mtgLen " +
                    "       from calendar_event_dao ced, calendar_event_participant_dao cepd, " +
                    "           email_dao ed, email_dao_people edp, person_dao pd , domain_dao dd, " +
                    "           domain_dao_organizations ddo " +
                    "       WHERE ced.id = cepd.calendar_event_id " +
                    "         and ed.id = cepd.email_id " +
                    "         and ed.id = edp.emails_id " +
                    "         and edp.people_id = pd.id " +
                    "         and `start` > :sDate and `start` < :eDate " +
                    "         and dd.id = ed.domain_id " +
                    "         and dd.id = ddo.domains_id " +
                    "         and ddo.organizations_id = :tenantId " +
                    "         and exists ( " +
                    "           select count(distinct domaindao26_.name) " +
                    "           from calendar_event_dao calendarev23_ " +
                    "           inner join " +
                    "               calendar_event_participant_dao participan24_ " +
                    "                   on calendarev23_.id=participan24_.calendar_event_id " +
                    "           inner join " +
                    "               email_dao emaildao25_ " +
                    "                   on participan24_.email_id=emaildao25_.id " +
                    "           inner join " +
                    "               domain_dao domaindao26_ " +
                    "                   on emaildao25_.domain_id=domaindao26_.id " +
                    "           where calendarev23_.id = ced.id " +
                    "           group by calendarev23_.id " +
                    "           having count(distinct domaindao26_.name) > 1 " +
                    "         ) " +
                    "         group by hex(ced.id) " +
                    "   ) as x"
            , nativeQuery = true
    )
    CalendarMeetingEfficacyView getExternalMeetingEfficacyAverages(
            UUID tenantId, Instant sDate, Instant eDate);

    @Query(
            value = " select " +
                    "   avg(totalTime) as averageStaffTimePerMeeting," +
                    "   avg(mtgLen) as averageMeetingLength " +
                    "   from ( " +
                    "       select sum(timestampdiff(second, ced.start, ced.end)) as TotalTime, " +
                    "               count(distinct pd.id) as attendees, " +
                    "               timestampdiff(second, ced.start, ced.end) as mtgLen " +
                    "       from calendar_event_dao ced, calendar_event_participant_dao cepd, " +
                    "           email_dao ed, email_dao_people edp, person_dao pd , domain_dao dd," +
                    "           domain_dao_organizations ddo, employee_dao emp, department_dao dept " +
                    "       WHERE ced.id = cepd.calendar_event_id " +
                    "         and ed.id = cepd.email_id " +
                    "         and ed.id = edp.emails_id " +
                    "         and edp.people_id = pd.id " +
                    "         and `start` > :sDate and `start` < :eDate " +
                    "         and dd.id = ed.domain_id " +
                    "         and dd.id = ddo.domains_id " +
                    "         and emp.person_id = pd.id " +
                    "         and emp.department_id = dept.id " +
                    "         and dept.name = :departmentName " +
                    "         and ddo.organizations_id = :tenantId " +
                    "         and exists ( " +
                    "           select count(distinct domaindao26_.name) " +
                    "           from calendar_event_dao calendarev23_ " +
                    "           inner join " +
                    "               calendar_event_participant_dao participan24_ " +
                    "                   on calendarev23_.id=participan24_.calendar_event_id " +
                    "           inner join " +
                    "               email_dao emaildao25_ " +
                    "                   on participan24_.email_id=emaildao25_.id " +
                    "           inner join " +
                    "               domain_dao domaindao26_ " +
                    "                   on emaildao25_.domain_id=domaindao26_.id " +
                    "           where calendarev23_.id = ced.id " +
                    "           group by calendarev23_.id " +
                    "           having count(distinct domaindao26_.name) = 1 " +
                    "         ) " +
                    "         group by hex(ced.id) " +
                    "   ) as x"
            , nativeQuery = true
    )
    CalendarMeetingEfficacyView getDepartmentInternalMeetingEfficacyAverages(
            UUID tenantId, Instant sDate, Instant eDate, String departmentName);

    @Query(
            value = " select " +
                    "   avg(totalTime) as averageStaffTimePerMeeting," +
                    "   avg(mtgLen) as averageMeetingLength " +
                    "   from ( " +
                    "       select sum(timestampdiff(second, ced.start, ced.end)) as TotalTime, " +
                    "               count(distinct pd.id) as attendees, " +
                    "               timestampdiff(second, ced.start, ced.end) as mtgLen " +
                    "       from calendar_event_dao ced, calendar_event_participant_dao cepd, " +
                    "           email_dao ed, email_dao_people edp, person_dao pd , domain_dao dd," +
                    "           domain_dao_organizations ddo, employee_dao emp, department_dao dept " +
                    "       WHERE ced.id = cepd.calendar_event_id " +
                    "         and ed.id = cepd.email_id " +
                    "         and ed.id = edp.emails_id " +
                    "         and edp.people_id = pd.id " +
                    "         and `start` > :sDate and `start` < :eDate " +
                    "         and dd.id = ed.domain_id " +
                    "         and dd.id = ddo.domains_id " +
                    "         and emp.person_id = pd.id " +
                    "         and emp.department_id = dept.id " +
                    "         and dept.name = :departmentName " +
                    "         and ddo.organizations_id = :tenantId " +
                    "         and exists ( " +
                    "           select count(distinct domaindao26_.name) " +
                    "           from calendar_event_dao calendarev23_ " +
                    "           inner join " +
                    "               calendar_event_participant_dao participan24_ " +
                    "                   on calendarev23_.id=participan24_.calendar_event_id " +
                    "           inner join " +
                    "               email_dao emaildao25_ " +
                    "                   on participan24_.email_id=emaildao25_.id " +
                    "           inner join " +
                    "               domain_dao domaindao26_ " +
                    "                   on emaildao25_.domain_id=domaindao26_.id " +
                    "           where calendarev23_.id = ced.id " +
                    "           group by calendarev23_.id " +
                    "           having count(distinct domaindao26_.name) > 1 " +
                    "         ) " +
                    "         group by hex(ced.id) " +
                    "   ) as x"
            , nativeQuery = true
    )
    CalendarMeetingEfficacyView getDepartmentExternalMeetingEfficacyAverages(
            UUID tenantId, Instant sDate, Instant eDate, String departmentName);
}