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
                "       created as created, " +
                "       person_dao.first_name, person_dao.last_name " +
                "  from calendar_event_dao,  " +
                "       calendar_event_participant_dao, " +
                "       email_dao," +
                "       email_dao_people," +
                "       person_dao, " +
                "       domain_dao_organizations " +
                " where calendar_event_participant_dao.calendar_event_id = calendar_event_dao.id " +
                "   and calendar_event_participant_dao.email_id = email_dao.id " +
                "   and email_dao.id = email_dao_people.emails_id " +
                "   and person_dao.id = email_dao_people.people_id " +
                "   and domain_dao_organizations.domains_id = email_dao.domain_id " +
                "   and calendar_event_dao.start between :sDate and :eDate " +
                "   and domain_dao_organizations.organizations_id = :tenantId "
            , nativeQuery = true
    )
    Page<CalendarEventsWithAttendeesViewInterface> getCalendarEventsAttendees(UUID tenantId, Instant sDate, Instant eDate, Pageable pageable);

}
