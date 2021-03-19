package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsWithAttendeesView;
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

    String internalCalendarEventsFromWhereClause =
            "  from calendar_event_dao `ced`, person_dao pd, email_dao_people edp, " +
                    "       email_dao ed, calendar_event_participant_dao cepd, domain_dao dd " +
                    " where ced.start between :sDate and :eDate " +
                    "       and pd.id = :organizer " +
                    "       and pd.id = edp.people_id " +
                    "       and edp.emails_id = ed.id " +
                    "       and cepd.email_id = ed.id " +
                    "       and cepd.organizer = 1 " +
                    "       and cepd.calendar_event_id = ced.id " +
                    "       and ed.domain_id = dd.id " +
                    "   and exists ( " +
                    "           select 'x' " +
                    "             from person_dao, email_dao_people edp2, email_dao ed2," +
                    "                  calendar_event_participant_dao cepd2, domain_dao_organizations ddo " +
                    "            where cepd2.calendar_event_id = ced.id " +
                    "              and cepd2.email_id = ed2.id " +
                    "              and ed2.domain_id = ddo.domains_id " +
                    "              and ed2.id = edp2.emails_id " +
                    "              and person_dao.id = edp2.people_id " +
                    "              and ddo.organizations_id = :tenantId " +
                    "       ) " +
                    "   and exists ( " +
                    "           select 'x' " +
                    "             from person_dao, email_dao_people edp3, email_dao ed3, " +
                    "                  calendar_event_participant_dao cepd3 " +
                    "            where person_dao.id = :organizer " +
                    "              and person_dao.id = edp3.people_id " +
                    "              and edp3.emails_id = ed3.id " +
                    "              and cepd3.email_id = ed3.id " +
                    "              and cepd3.organizer = 1 " +
                    "              and cepd3.calendar_event_id = ced.id " +
                    "       ) " +
                    "   and exists (" +
                    "               select count(distinct dd4.name)" +
                    "               from calendar_event_participant_dao cepd4, email_dao ed4, domain_dao dd4 " +
                    "               where cepd4.calendar_event_id = ced.id " +
                    "                   and cepd4.email_id = ed4.id " +
                    "                   and ed4.domain_id = dd4.id " +
                    "               having count(distinct dd4.name) = 1" +
                    "   )";

    // Get all the calendar events where a person from this organization was an attendee
    @Query(
            value = "select ced.id as id," +
                    "       google_calendar_id as googleCalendarId," +
                    "       summary as summary, " +
                    "       start as start, " +
                    "       end as end, " +
                    "       created as created, " +
                    "       ( select count(*) " +
                    "           from person_dao, " +
                    "           email_dao_people edp2, " +
                    "           email_dao ed2, " +
                    "           calendar_event_participant_dao cepd2, " +
                    "           domain_dao_organizations ddo " +
                    "         where cepd2.calendar_event_id = ced.id " +
                    "           and cepd2.email_id = ed2.id " +
                    "           and ed2.domain_id = ddo.domains_id " +
                    "           and ed2.id = edp2.emails_id " +
                    "           and person_dao.id = edp2.people_id " +
                    "           and ddo.organizations_id = :tenantId " +
                    "       ) as numberInternalAttendees, " +
                    "       ( select count(*) " +
                    "           from person_dao, " +
                    "           email_dao_people edp2, " +
                    "           email_dao ed2, " +
                    "           calendar_event_participant_dao cepd2, " +
                    "           domain_dao_organizations ddo " +
                    "         where cepd2.calendar_event_id = ced.id " +
                    "           and cepd2.email_id = ed2.id " +
                    "           and ed2.domain_id = ddo.domains_id " +
                    "           and ed2.id = edp2.emails_id " +
                    "           and person_dao.id = edp2.people_id " +
                    "       ) as numberTotalAttendees, " +
                    "       (select numberInternalAttendees) * timestampdiff(second, ced.start, ced.end) as totalSeconds, " +
                    "       pd.first_name as organizerFirstName, " +
                    "       pd.last_name as organizerLastName, " +
                    "       concat(ed.user, '@', dd.name) as organizerEmail " +
                    internalCalendarEventsFromWhereClause +
                    " having numberInternalAttendees > 0 "
            ,countQuery = "select ced.id as id," +
                    "       ( select count(*) " +
                    "           from person_dao, " +
                    "           email_dao_people edp2, " +
                    "           email_dao ed2, " +
                    "           calendar_event_participant_dao cepd2, " +
                    "           domain_dao_organizations ddo " +
                    "         where cepd2.calendar_event_id = ced.id " +
                    "           and cepd2.email_id = ed2.id " +
                    "           and ed2.domain_id = ddo.domains_id " +
                    "           and ed2.id = edp2.emails_id " +
                    "           and person_dao.id = edp2.people_id " +
                    "           and ddo.organizations_id = :tenantId " +
                    "       ) as numberInternalAttendees " +
                    internalCalendarEventsFromWhereClause +
                    " having numberInternalAttendees > 0 "
            , nativeQuery = true
    )
    Page<CalendarEventsWithAttendeesView> getInternalCalendarEvents(
            UUID tenantId, Instant sDate, Instant eDate, UUID organizer, Pageable pageable);


    String externalCalendarEventsFromWhereClause =
            "  from calendar_event_dao `ced`, person_dao pd, email_dao_people edp, " +
                    "       email_dao ed, calendar_event_participant_dao cepd, domain_dao dd " +
                    " where ced.start between :sDate and :eDate " +
                    "       and pd.id = :organizer " +
                    "       and pd.id = edp.people_id " +
                    "       and edp.emails_id = ed.id " +
                    "       and cepd.email_id = ed.id " +
                    "       and cepd.organizer = 1 " +
                    "       and cepd.calendar_event_id = ced.id " +
                    "       and ed.domain_id = dd.id " +
                    "   and exists ( " +
                    "           select 'x' " +
                    "             from person_dao, email_dao_people edp2, email_dao ed2," +
                    "                  calendar_event_participant_dao cepd2, domain_dao_organizations ddo2 " +
                    "            where cepd2.calendar_event_id = ced.id " +
                    "              and cepd2.email_id = ed2.id " +
                    "              and ed2.domain_id = ddo2.domains_id " +
                    "              and ed2.id = edp2.emails_id " +
                    "              and person_dao.id = edp2.people_id " +
                    "              and ddo2.organizations_id = :tenantId " +
                    "       )" ;

    @Query(
            value = "select " +
                    "       ced.id as id," +
                    "       google_calendar_id as googleCalendarId," +
                    "       summary as summary, " +
                    "       start as start, " +
                    "       end as end, " +
                    "       created as created, " +
                    "       ( select count(*) " +
                    "           from person_dao, " +
                    "           email_dao_people edp2, " +
                    "           email_dao ed2, " +
                    "           calendar_event_participant_dao cepd2, " +
                    "           domain_dao_organizations ddo " +
                    "         where cepd2.calendar_event_id = ced.id " +
                    "           and cepd2.email_id = ed2.id " +
                    "           and ed2.domain_id = ddo.domains_id " +
                    "           and ed2.id = edp2.emails_id " +
                    "           and person_dao.id = edp2.people_id " +
                    "           and ddo.organizations_id = :tenantId " +
                    "       ) as numberInternalAttendees, " +
                    "       ( select count(*) " +
                    "           from calendar_event_participant_dao " +
                    "         where calendar_event_id = ced.id " +
                    "       ) as numberTotalAttendees, " +
                    "       (select numberInternalAttendees) * timestampdiff(second, ced.start, ced.end) as totalSeconds, " +
                    "       pd.first_name as organizerFirstName, " +
                    "       pd.last_name as organizerLastName, " +
                    "       concat(ed.user, '@', dd.name) as organizerEmail " +
                    externalCalendarEventsFromWhereClause +
                    " having numberInternalAttendees > 0 "

            , countQuery =  "select " +
            "       ced.id as id," +
            "       ( select count(*) " +
            "           from person_dao, " +
            "           email_dao_people edp2, " +
            "           email_dao ed2, " +
            "           calendar_event_participant_dao cepd2, " +
            "           domain_dao_organizations ddo " +
            "         where cepd2.calendar_event_id = ced.id " +
            "           and cepd2.email_id = ed2.id " +
            "           and ed2.domain_id = ddo.domains_id " +
            "           and ed2.id = edp2.emails_id " +
            "           and person_dao.id = edp2.people_id " +
            "           and ddo.organizations_id = :tenantId " +
            "       ) as numberInternalAttendees " +
            externalCalendarEventsFromWhereClause +
            " having numberInternalAttendees > 0 "
            , nativeQuery = true
    )
    Page<CalendarEventsWithAttendeesView> getExternalCalendarEvents(
            UUID tenantId, Instant sDate, Instant eDate, UUID organizer, Pageable pageable);
}
