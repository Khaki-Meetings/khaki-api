package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.TimeBlockSummaryDao;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsEmployeeTimeView;
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

    @Query(
            value = "select (" +
            "       5 * (timestampdiff(day, :sDate, :eDate) / 7) " +
            "       + SUBSTRING('0123444401233334012222340111123400001234000123440', " +
            "       7 * (dayofweek(:sDate) - 1) + dayofweek(:eDate), 1) " +
            "    ) from dual "
            , nativeQuery = true)
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
                    "     where cap_count.calendarEvent = calendarEvent " +
                    "       and org_count.id = :tenantId" +
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
                    "     where cap_count.calendarEvent = calendarEvent " +
                    "       and org_count.id = :tenantId" +
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
                    "   where organization.id = :tenantId ) as numEmployees, " +
                    "count(calendarEventParticipant.calendarEvent) as meetingCount " +
                    "from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   inner join employees.person as person " +
                    "   inner join person.emails as emails " +
                    "   inner join CalendarEventParticipantDao calendarEventParticipant " +
                    "       on calendarEventParticipant.email = emails.id " +
                    "           and calendarEventParticipant.organizer = true " +
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
                    "   )"
    )
    TimeBlockSummaryView findExternalTimeBlockSummaryInRange(Instant sDate, Instant eDate, UUID tenantId);

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
                    "sum(" +
                    "   timestampdiff(second, calendarEvent.start, calendarEvent.end)" +
                    ") as meetingLengthSeconds, " +
                    " sum(" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "     where cap_count.calendarEvent = calendarEvent " +
                    ") as totalMeetingAttendees, " +
                    " sum((" +
                    "     select count(*) " +
                    "     from CalendarEventParticipantDao as cap_count " +
                    "       inner join cap_count.email as email_count" +
                    "       inner join email_count.domain as domain_count" +
                    "       inner join domain_count.organizations as org_count" +
                    "     where cap_count.calendarEvent = calendarEvent " +
                    "       and org_count.id = :tenantId" +
                    ") " +
                    ") as totalInternalMeetingAttendees, " +
                    " ( select count(*) " +
                    "   from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   where organization.id = :tenantId ) as numEmployees, " +
                    "count(calendarEventParticipant.calendarEvent) as meetingCount " +
                    "from OrganizationDao organization " +
                    "   inner join organization.departments as departments " +
                    "   inner join departments.employees as employees " +
                    "   inner join employees.person as person " +
                    "   inner join person.emails as emails " +
                    "   inner join CalendarEventParticipantDao calendarEventParticipant " +
                    "       on calendarEventParticipant.email = emails.id " +
                    "           and calendarEventParticipant.organizer = true " +
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
                    "   )"
    )
    TimeBlockSummaryView findInternalTimeBlockSummaryInRange(Instant sDate, Instant eDate, UUID tenantId);

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

    String x = "(5 * (timestampdiff(day, :sDate, :eDate) / 7) " +
            "  + SUBSTRING('0123444401233334012222340111123400001234000123440', " +
            "  7 * (dayofweek(:sDate) - 1) + dayofweek(:eDate), 1) " +
            " ) ";
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
                    "               (" + x + " * 8 * 60)) * 100 > :minThreshold " +
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
}
