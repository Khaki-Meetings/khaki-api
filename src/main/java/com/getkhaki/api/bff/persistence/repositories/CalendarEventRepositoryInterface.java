package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsWithAttendeesViewInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalendarEventRepositoryInterface extends JpaRepository<CalendarEventDao, UUID> {
    Optional<CalendarEventDao> findDistinctByGoogleCalendarId(String googleCalendarId);

    // Get all the calendar events where a person from this organization was an attendee
    @Query(
        value = "select calendar_event_dao.id as id," +
                "       google_calendar_id as googleCalendarId," +
                "       summary as summary, " +
                "       start as start, " +
                "       end as end, " +
                "       created as created " +
                "  from calendar_event_dao  " +
                " where calendar_event_dao.start between :sDate and :eDate " +
                "   and exists ( " +
                "           select 'x' " +
                "             from person_dao pd2, email_dao_people edp2, email_dao ed2," +
                "                  calendar_event_participant_dao cepd2, domain_dao_organizations ddo " +
                "            where cepd2.calendar_event_id = calendar_event_dao.id " +
                "              and cepd2.email_id = ed2.id " +
                "              and ed2.domain_id = ddo.domains_id " +
                "              and ed2.id = edp2.emails_id " +
                "              and pd2.id = edp2.people_id " +
                "              and ddo.organizations_id = :tenantId " +
                "       ) " +
                "   and exists ( " +
                "           select 'x' " +
                "             from person_dao pd3, email_dao_people edp3, email_dao ed3, " +
                "                  calendar_event_participant_dao cepd3 " +
                "            where pd3.id = unhex(:organizer) " +
                "              and pd3.id = edp3.people_id " +
                "              and edp3.emails_id = ed3.id " +
                "              and cepd3.email_id = ed3.id " +
                "              and cepd3.organizer = 1 " +
                "              and cepd3.calendar_event_id = calendar_event_dao.id " +
                "       ) "
            , nativeQuery = true
    )
    Page<CalendarEventsWithAttendeesViewInterface> getCalendarEventsAttendees(
            UUID tenantId, Instant sDate, Instant eDate, String organizer, Pageable pageable);

    String query =
            "  from calendar_event_dao  " +
            " where calendar_event_dao.start between :sDate and :eDate " +
            "   and exists ( " +
            "           select 'x' " +
            "             from person_dao pd2, email_dao_people edp2, email_dao ed2," +
            "                  calendar_event_participant_dao cepd2, domain_dao_organizations ddo " +
            "            where cepd2.calendar_event_id = calendar_event_dao.id " +
            "              and cepd2.email_id = ed2.id " +
            "              and ed2.domain_id = ddo.domains_id " +
            "              and ed2.id = edp2.emails_id " +
            "              and pd2.id = edp2.people_id " +
            "              and ddo.organizations_id = :tenantId " +
            "       ) ";

    // Get all the calendar events where a person from this organization was an attendee
    @Query(
            value = " select calendar_event_dao.id as id," +
                    "       google_calendar_id as googleCalendarId," +
                    "       summary as summary, " +
                    "       start as start, " +
                    "       end as end, " +
                    "       created as created " + query,
            countQuery = " select count(*) " + query,
            nativeQuery = true
    )
    Page<CalendarEventsWithAttendeesViewInterface> getCalendarEventsAttendeesWithoutOrganizer(
            UUID tenantId, Instant sDate, Instant eDate, Pageable pageable);
}
