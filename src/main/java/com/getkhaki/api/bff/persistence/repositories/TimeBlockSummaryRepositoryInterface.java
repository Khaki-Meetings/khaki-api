package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.models.views.TimeBlockSummaryView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
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
}
