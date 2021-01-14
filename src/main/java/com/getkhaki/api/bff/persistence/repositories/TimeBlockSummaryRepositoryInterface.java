package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.models.views.TimeBlockSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TimeBlockSummaryRepositoryInterface extends JpaRepository<CalendarEventDao, UUID> {
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
                    ") as totalSeconds," +
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
                    "   and calendarEvent.start between :sDate and :eDate"
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
                    ") as totalSeconds," +
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


//    select pd.first_name,
//    count(cepd.calendar_event_id) as totalMeetings,
//       (
//    select sum(timestampdiff(second, ced.start, ced.end))
//    from calendar_event_dao where calendar_event_dao.id = ced.id
//       )                             as totalSeconds
//
//    from person_dao pd
//    inner join email_dao_people edp on pd.id = edp.people_id
//    inner join email_dao ed on edp.emails_id = ed.id
//    inner join domain_dao dd on ed.domain_id = dd.id
//    inner join domain_dao_organizations ddo on dd.id = ddo.domains_id
//    inner join organization_dao od on ddo.organizations_id = od.id
//    inner join calendar_event_participant_dao cepd on ed.id = cepd.email_id
//    inner join calendar_event_dao ced on cepd.calendar_event_id = ced.id
//    where od.name = 'S56'
//    group by pd.id
//    ;


    @Query(
            "select person.firstName, " +
                    "(" +
                    "   select sum(timestampdiff(second, innerCalendarEvent.start, innerCalendarEvent.end))" +
                    "   from CalendarEventDao innerCalendarEvent" +
                    "   where innerCalendarEvent.id = calendarEvent.id" +
                    ") as totalSeconds," +
                    "count(calendarEvent) as meetingCount " +
                    "from PersonDao person " +
                    "   inner join person.emails as emails" +
                    "   inner join emails.domain.organizations organization" +
                    "   inner join CalendarEventParticipantDao calendarEventParticipants" +
                    "       on calendarEventParticipants.email.id = emails.id" +
                    "   inner join calendarEventParticipants.calendarEvent calendarEvent " +
                    "where organization.id = :tenantId " +
                    "   and person.id = :personId " +
                    "   and calendarEvent.start between :sDate and :eDate " +
                    "group by calendarEvent"
    )
    List<TimeBlockSummaryView> findIndividualExternalTimeBlockSummaryInRange(
            UUID personId,
            Instant sDate,
            Instant eDate,
            UUID tenantId
    );

//    @Query("select 2 as totalSeconds, 4 as meetingCount from PersonDao")
//    TimeBlockSummaryView findIndividualInternalTimeBlockSummaryInRange(
//            UUID personId,
//            Instant sDate,
//            Instant eDate,
//            UUID tenantId
//    );
}
