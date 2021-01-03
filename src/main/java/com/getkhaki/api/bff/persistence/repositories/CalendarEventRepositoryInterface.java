package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsWithAttendeesView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalendarEventRepositoryInterface extends JpaRepository<CalendarEventDao, UUID> {
    Optional<CalendarEventDao> findDistinctByGoogleCalendarId(String googleCalendarId);

    /*
    select ced.id, ced.summary, ced.start, ced.end, count(pd.id) from person_dao pd, email_dao_people edp, email_dao ed,
	domain_dao dd, domain_dao_organizations ddo, organization_dao od,
	calendar_event_participant_dao cepd, calendar_event_dao ced
 where pd.id = edp.people_id
   and ed.id = edp.emails_id
  X and ed.domain_id = dd.id
   and ddo.domains_id = dd.id
   and ddo.organizations_id = od.id
  X and cepd.email_id = ed.id
  X and ced.id = cepd.calendar_event_id
   and od.name = 'Khaki'
     */
/*
    @Query(
        value = "select google_calendar_id as googleCalendarId," +
                    "    summary as summary, " +
                "   person_dao.id  " +
                    "  from calendar_event_dao,  " +
                    "    calendar_event_participant_dao, " +

                " email_dao," +
                " email_dao_people," +
                " person_dao  " +
                    "  where calendar_event_participant_dao.calendar_event_id = calendar_event_dao.id " +
                "   and calendar_event_participant_dao.email_id = email_dao.id " +
                " and email_dao.id = email_dao_people.emails_id " +
                " and person_dao.id = email_dao_people.people_id "
        , nativeQuery = true
    ) */
   // @Query(
          /*value = "select calendarEventDao.googleCalendarId as googleCalendarId," +
                  "     calendarEventDao.summary as summary, " +
                    "    persons as participants" +
                    "  from CalendarEventDao calendarEventDao " +
                    "  left join CalendarEventParticipantDao calendarEventParticipantDao " +
                    "    on calendarEventParticipantDao.calendarEvent = calendarEventDao " +
                    "  left join EmailDao emailDao " +
                    "    on calendarEventParticipantDao.email = emailDao" +
                    " inner join emailDao.domain domain " +
                    " inner join domain.organizations organizations" +
        //          " inner join emailDao.people persons" +
                  " inner join emailDao.people persons " +
                    " where organizations.id = :tenantId "
            */
    /*    value = " select calendarEventDao.googleCalendarId as googleCalendarId, " +
                "  calendarEventDao.summary as summary, " +
                "  people as participants "+
                "  from CalendarEventDao calendarEventDao," +
                "   CalendarEventParticipantDao calendarEventParticipantDao, " +
                "   EmailDao emailDao " +
              //  " inner join domain.organizations organizations" +

               "   inner join emailDao.people people" +
        //        from person_dao pd, email_dao_people edp, email_dao ed,
        //    domain_dao dd, domain_dao_organizations ddo, organization_dao od,
        //    calendar_event_participant_dao cepd, calendar_event_dao ced


            " where calendarEventDao = calendarEventParticipantDao.calendarEvent " +
            " and emailDao = calendarEventParticipantDao.email "
       //     " and "
        //        " left join fetch emailDao.people persons"
         //   " and personDao  emailDao.people "

    )*/
    @Query(
        value = "select google_calendar_id as googleCalendarId," +
            "    summary as summary, " +
            "   person_dao.first_name, person_dao.last_name " +
            "  from calendar_event_dao,  " +
            "    calendar_event_participant_dao, " +
            " email_dao," +
            " email_dao_people," +
            " person_dao  " +
            "  where calendar_event_participant_dao.calendar_event_id = calendar_event_dao.id " +
            "   and calendar_event_participant_dao.email_id = email_dao.id " +
            " and email_dao.id = email_dao_people.emails_id " +
            " and person_dao.id = email_dao_people.people_id "
            , nativeQuery = true


            /* " select calendarEventDao.googleCalendarId as googleCalendarId, " +
            "  calendarEventDao.summary as summary, " +
            "  persons as participants "+
            "  from CalendarEventDao calendarEventDao" +
            "  left join CalendarEventParticipantDao calendarEventParticipantDao " +
            "    on calendarEventParticipantDao.calendarEvent = calendarEventDao " +
            "  left join EmailDao emailDao " +
            "    on calendarEventParticipantDao.email = emailDao" +
            "  inner join emailDao.people persons"*
*/
           /* "select " +
                    " calendarEvent.googleCalendarId as googleCalendarId, " +
            "  calendarEvent.summary as summary," +
                    " person as participants " +
            "   from OrganizationDao organization " +
            "   inner join organization.departments as departments " +
            "   inner join departments.employees as employees " +
            "   inner join employees.person as person " +
            "   inner join person.emails as emails " +
            "   inner join CalendarEventParticipantDao calendarEventParticipant " +
            "       on calendarEventParticipant.email = emails.id " +
            "           and calendarEventParticipant.organizer = true " +
            "   inner join calendarEventParticipant.calendarEvent as calendarEvent " +
            "where organization.id = :tenantId "*/
    )
    Page<CalendarEventsWithAttendeesView> getCalendarEventsAttendees(UUID tenantId, Pageable pageable);

}
