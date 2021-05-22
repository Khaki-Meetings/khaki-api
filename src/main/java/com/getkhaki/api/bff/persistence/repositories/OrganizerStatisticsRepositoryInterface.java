package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventParticipantDao;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsAggregateView;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface OrganizerStatisticsRepositoryInterface extends JpaRepository<CalendarEventParticipantDao, UUID> {
    @Query(
            value = "select " +
                    "   (" +
                    "       select count(organizerMeetSecondsCepD.id) " +
                    "       from CalendarEventParticipantDao organizerMeetSecondsCepD " +
                    "       where organizerMeetSecondsCepD.email = organizerCalendarEventParticipantDao.email " +
                    "           and organizerMeetSecondsCepD.organizer = true " +
                    "           and organizerMeetSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "   ) as totalMeetings," +
                    "   (" +
                    "       select" +
                    "           sum(" +
                    "                   timestampdiff(" +
                    "                       second," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.start," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.end" +
                    "                   )" +
                    "           )" +
                    "       from CalendarEventParticipantDao organizerTotalSecondsCepD" +
                    "           left join CalendarEventParticipantDao totalSecondsParticipantsCepD " +
                    "               on organizerTotalSecondsCepD.calendarEvent = totalSecondsParticipantsCepD.calendarEvent " +
                    "           inner join totalSecondsParticipantsCepD.email.domain.organizations as organizationTotalSeconds" +
                    "       where organizerTotalSecondsCepD.email = organizerCalendarEventParticipantDao.email" +
                    "           and organizerTotalSecondsCepD.organizer = true" +
                    "           and organizerTotalSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and organizationTotalSeconds.id = :tenantId" +
                    "   ) as totalSeconds," +
                    "   concat(organizerEmailDao.user, '@', organizerDomainDao.name) as organizerEmail, " +
                    "   concat(peopleDao.firstName) as organizerFirstName, " +
                    "   concat(peopleDao.lastName) as organizerLastName, " +
                    "   concat(peopleDao.avatarUrl) as organizerAvatarUrl, " +
                    "   CONCAT(SUBSTR(hex(peopleDao.id), 1, 8), '-', SUBSTR(hex(peopleDao.id), 9, 4), '-', " +
                    "        SUBSTR(hex(peopleDao.id), 13, 4), '-', SUBSTR(hex(peopleDao.id), 17, 4), '-', SUBSTR(hex(peopleDao.id), 21, 12)) as organizerId " +
                    "from CalendarEventParticipantDao organizerCalendarEventParticipantDao " +
                    "   inner join organizerCalendarEventParticipantDao.email organizerEmailDao" +
                    "   inner join organizerEmailDao.domain organizerDomainDao" +
                    "   inner join organizerCalendarEventParticipantDao.calendarEvent organizerCalendarEvent " +
                    "   inner join organizerDomainDao.organizations tenant " +
                    "   inner join organizerEmailDao.people peopleDao " +
                    "where tenant.id = :tenantId" +
                    "   and organizerCalendarEventParticipantDao.organizer = true " +
                    "   and organizerCalendarEventParticipantDao.calendarEvent.start between :sDate and :eDate " +
                    "group by organizerCalendarEventParticipantDao.email",
            countQuery = "select count(distinct cepd.email)" +
                    " from CalendarEventParticipantDao cepd" +
                    "   inner join cepd.calendarEvent ced" +
                    "   inner join cepd.email ed" +
                    "   inner join ed.people" +
                    "   inner join ed.domain dd" +
                    "   inner join dd.organizations org " +
                    "where org.id = :tenantId" +
                    "   and ced.start between :sDate and :eDate" +
                    "   and cepd.organizer = true"
    )
    Page<OrganizerStatisticsView> findExternalOrganizerStatistics(Instant sDate, Instant eDate, UUID tenantId, Pageable pageable);


    @Query(
            value = "select " +
                    "   (" +
                    "       select count(organizerMeetSecondsCepD.id) " +
                    "       from CalendarEventParticipantDao organizerMeetSecondsCepD " +
                    "       where organizerMeetSecondsCepD.email = organizerCalendarEventParticipantDao.email " +
                    "           and organizerMeetSecondsCepD.organizer = true " +
                    "           and organizerMeetSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and exists (" +
                    "               select count(distinct domain.name)" +
                    "               from CalendarEventDao innerCalendarEvent" +
                    "               inner join innerCalendarEvent.participants innerParticipants" +
                    "               inner join innerParticipants.email.domain domain" +
                    "               where innerCalendarEvent = organizerMeetSecondsCepD.calendarEvent" +
                    "               group by innerCalendarEvent" +
                    "               having count(distinct domain.name) = 1" +
                    "           )" +
                    "   ) as totalMeetings," +
                    "   (" +
                    "       select" +
                    "           sum(" +
                    "                   timestampdiff(" +
                    "                       second," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.start," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.end" +
                    "                   )" +
                    "           )" +
                    "       from CalendarEventParticipantDao organizerTotalSecondsCepD" +
                    "           left join CalendarEventParticipantDao totalSecondsParticipantsCepD " +
                    "               on organizerTotalSecondsCepD.calendarEvent = totalSecondsParticipantsCepD.calendarEvent " +
                    "           inner join totalSecondsParticipantsCepD.email.domain.organizations as organizationTotalSeconds" +
                    "       where organizerTotalSecondsCepD.email = organizerCalendarEventParticipantDao.email" +
                    "           and organizerTotalSecondsCepD.organizer = true" +
                    "           and organizerTotalSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and organizationTotalSeconds.id = :tenantId" +
                    "           and exists (" +
                    "               select count(distinct domain.name)" +
                    "               from CalendarEventDao innerCalendarEvent" +
                    "               inner join innerCalendarEvent.participants innerParticipants" +
                    "               inner join innerParticipants.email.domain domain" +
                    "               where innerCalendarEvent = organizerTotalSecondsCepD.calendarEvent" +
                    "               group by innerCalendarEvent" +
                    "               having count(distinct domain.name) = 1" +
                    "           )" +
                    "   ) as totalSeconds," +
                    "   concat(organizerEmailDao.user, '@', organizerDomainDao.name) as organizerEmail, " +
                    "   concat(peopleDao.firstName) as organizerFirstName, " +
                    "   concat(peopleDao.lastName) as organizerLastName, " +
                    "   concat(peopleDao.avatarUrl) as organizerAvatarUrl, " +
                    "   CONCAT(SUBSTR(hex(peopleDao.id), 1, 8), '-', SUBSTR(hex(peopleDao.id), 9, 4), '-', " +
                    "        SUBSTR(hex(peopleDao.id), 13, 4), '-', SUBSTR(hex(peopleDao.id), 17, 4), '-', SUBSTR(hex(peopleDao.id), 21, 12)) as organizerId " +
                    "from CalendarEventParticipantDao organizerCalendarEventParticipantDao " +
                    "   inner join organizerCalendarEventParticipantDao.email organizerEmailDao" +
                    "   inner join organizerEmailDao.domain organizerDomainDao" +
                    "   inner join organizerCalendarEventParticipantDao.calendarEvent organizerCalendarEvent " +
                    "   inner join organizerDomainDao.organizations tenant " +
                    "   inner join organizerEmailDao.people peopleDao " +
                    "where tenant.id = :tenantId" +
                    "   and organizerCalendarEventParticipantDao.organizer = true " +
                    "   and organizerCalendarEventParticipantDao.calendarEvent.start between :sDate and :eDate " +
                    "   and exists (" +
                    "       select count(distinct domain.name)" +
                    "       from CalendarEventDao innerCalendarEvent" +
                    "       inner join innerCalendarEvent.participants innerParticipants" +
                    "       inner join innerParticipants.email.domain domain" +
                    "       where innerCalendarEvent = organizerCalendarEvent" +
                    "       group by innerCalendarEvent" +
                    "       having count(distinct domain.name) = 1" +
                    "   )" +
                    "group by organizerCalendarEventParticipantDao.email",
            countQuery = "select count(distinct cepd.email)" +
                    " from CalendarEventParticipantDao cepd" +
                    "   inner join cepd.calendarEvent ced" +
                    "   inner join cepd.email ed" +
                    "   inner join ed.people" +
                    "   inner join ed.domain dd" +
                    "   inner join dd.organizations org " +
                    "where org.id = :tenantId" +
                    "   and ced.start between :sDate and :eDate" +
                    "   and cepd.organizer = true" +
                    "   and exists (" +
                    "       select count(distinct domain.name)" +
                    "       from CalendarEventDao innerCalendarEvent" +
                    "       inner join innerCalendarEvent.participants innerParticipants" +
                    "       inner join innerParticipants.email.domain domain" +
                    "       where innerCalendarEvent = ced" +
                    "       group by innerCalendarEvent" +
                    "       having count(distinct domain.name) = 1" +
                    "   )"
    )
    Page<OrganizerStatisticsView> findInternalOrganizerStatistics(Instant sDate, Instant eDate, UUID tenantId, Pageable pageable);

    @Query(
            value = "select " +
                    "   (" +
                    "       select count(organizerMeetSecondsCepD.id) " +
                    "       from CalendarEventParticipantDao organizerMeetSecondsCepD " +
                    "       where organizerMeetSecondsCepD.email = organizerCalendarEventParticipantDao.email " +
                    "           and organizerMeetSecondsCepD.organizer = true " +
                    "           and organizerMeetSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and exists (" +
                    "               select count(distinct domain.name)" +
                    "               from CalendarEventDao innerCalendarEvent" +
                    "               inner join innerCalendarEvent.participants innerParticipants" +
                    "               inner join innerParticipants.email.domain domain" +
                    "               where innerCalendarEvent = organizerMeetSecondsCepD.calendarEvent" +
                    "               group by innerCalendarEvent" +
                    "               having count(distinct domain.name) = 1" +
                    "           )" +
                    "   ) as internalMeetingCount," +
                    "   COALESCE(" +
                    "   (" +
                    "       select" +
                    "           sum(" +
                    "                   timestampdiff(" +
                    "                       second," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.start," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.end" +
                    "                   )" +
                    "           )" +
                    "       from CalendarEventParticipantDao organizerTotalSecondsCepD" +
                    "           left join CalendarEventParticipantDao totalSecondsParticipantsCepD " +
                    "               on organizerTotalSecondsCepD.calendarEvent = totalSecondsParticipantsCepD.calendarEvent " +
                    "           inner join totalSecondsParticipantsCepD.email.domain.organizations as organizationTotalSeconds" +
                    "       where organizerTotalSecondsCepD.email = organizerCalendarEventParticipantDao.email" +
                    "           and organizerTotalSecondsCepD.organizer = true" +
                    "           and organizerTotalSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and organizationTotalSeconds.id = :tenantId" +
                    "           and exists (" +
                    "               select count(distinct domain.name)" +
                    "               from CalendarEventDao innerCalendarEvent" +
                    "               inner join innerCalendarEvent.participants innerParticipants" +
                    "               inner join innerParticipants.email.domain domain" +
                    "               where innerCalendarEvent = organizerTotalSecondsCepD.calendarEvent" +
                    "               group by innerCalendarEvent" +
                    "               having count(distinct domain.name) = 1" +
                    "           )" +
                    "   ), 0) as internalMeetingSeconds," +
                    "   (" +
                    "       select count(organizerMeetSecondsCepD.id) " +
                    "       from CalendarEventParticipantDao organizerMeetSecondsCepD " +
                    "       where organizerMeetSecondsCepD.email = organizerCalendarEventParticipantDao.email " +
                    "           and organizerMeetSecondsCepD.organizer = true " +
                    "           and organizerMeetSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and exists (" +
                    "               select count(distinct domain.name)" +
                    "               from CalendarEventDao innerCalendarEvent" +
                    "               inner join innerCalendarEvent.participants innerParticipants" +
                    "               inner join innerParticipants.email.domain domain" +
                    "               where innerCalendarEvent = organizerMeetSecondsCepD.calendarEvent" +
                    "               group by innerCalendarEvent" +
                    "               having count(distinct domain.name) > 1" +
                    "           ) " +
                    "   ) as externalMeetingCount," +
                    "   COALESCE(" +
                    "   (" +
                    "       select" +
                    "           sum(" +
                    "                   timestampdiff(" +
                    "                       second," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.start," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.end" +
                    "                   )" +
                    "           )" +
                    "       from CalendarEventParticipantDao organizerTotalSecondsCepD" +
                    "           left join CalendarEventParticipantDao totalSecondsParticipantsCepD " +
                    "               on organizerTotalSecondsCepD.calendarEvent = totalSecondsParticipantsCepD.calendarEvent " +
                    "           inner join totalSecondsParticipantsCepD.email.domain.organizations as organizationTotalSeconds" +
                    "       where organizerTotalSecondsCepD.email = organizerCalendarEventParticipantDao.email" +
                    "           and organizerTotalSecondsCepD.organizer = true" +
                    "           and organizerTotalSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and organizationTotalSeconds.id = :tenantId" +
                    "   ), 0) as externalMeetingSeconds," +
                    "   concat(organizerEmailDao.user, '@', organizerDomainDao.name) as organizerEmail, " +
                    "   concat(peopleDao.firstName) as organizerFirstName, " +
                    "   concat(peopleDao.lastName) as organizerLastName, " +
                    "   concat(peopleDao.avatarUrl) as organizerAvatarUrl, " +
                    "   CONCAT(SUBSTR(hex(peopleDao.id), 1, 8), '-', SUBSTR(hex(peopleDao.id), 9, 4), '-', " +
                    "        SUBSTR(hex(peopleDao.id), 13, 4), '-', SUBSTR(hex(peopleDao.id), 17, 4), '-', SUBSTR(hex(peopleDao.id), 21, 12)) as organizerId " +
                    "from CalendarEventParticipantDao organizerCalendarEventParticipantDao " +
                    "   inner join organizerCalendarEventParticipantDao.email organizerEmailDao" +
                    "   inner join organizerEmailDao.domain organizerDomainDao" +
                    "   inner join organizerCalendarEventParticipantDao.calendarEvent organizerCalendarEvent " +
                    "   inner join organizerDomainDao.organizations tenant " +
                    "   inner join organizerEmailDao.people peopleDao " +
                    "where tenant.id = :tenantId" +
                    "   and organizerCalendarEventParticipantDao.organizer = true " +
                    "   and organizerCalendarEventParticipantDao.calendarEvent.start between :sDate and :eDate " +
                    "group by organizerCalendarEventParticipantDao.email",
            countQuery = "select count(distinct cepd.email)" +
                    " from CalendarEventParticipantDao cepd" +
                    "   inner join cepd.calendarEvent ced" +
                    "   inner join cepd.email ed" +
                    "   inner join ed.people" +
                    "   inner join ed.domain dd" +
                    "   inner join dd.organizations org " +
                    "where org.id = :tenantId" +
                    "   and ced.start between :sDate and :eDate" +
                    "   and cepd.organizer = true"
    )
    Page<OrganizerStatisticsAggregateView> findAllOrganizerStatistics(Instant sDate, Instant eDate, UUID tenantId, Pageable pageable);

    @Query(
            value = "select " +
                    "   (" +
                    "       select count(organizerMeetSecondsCepD.id) " +
                    "       from CalendarEventParticipantDao organizerMeetSecondsCepD " +
                    "       where organizerMeetSecondsCepD.email = organizerCalendarEventParticipantDao.email " +
                    "           and organizerMeetSecondsCepD.organizer = true " +
                    "           and organizerMeetSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and exists (" +
                    "               select count(distinct domain.name)" +
                    "               from CalendarEventDao innerCalendarEvent" +
                    "               inner join innerCalendarEvent.participants innerParticipants" +
                    "               inner join innerParticipants.email.domain domain" +
                    "               where innerCalendarEvent = organizerMeetSecondsCepD.calendarEvent" +
                    "               group by innerCalendarEvent" +
                    "               having count(distinct domain.name) = 1" +
                    "           )" +
                    "   ) as internalMeetingCount," +
                    "   COALESCE(" +
                    "   (" +
                    "       select" +
                    "           sum(" +
                    "                   timestampdiff(" +
                    "                       second," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.start," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.end" +
                    "                   )" +
                    "           )" +
                    "       from CalendarEventParticipantDao organizerTotalSecondsCepD" +
                    "           left join CalendarEventParticipantDao totalSecondsParticipantsCepD " +
                    "               on organizerTotalSecondsCepD.calendarEvent = totalSecondsParticipantsCepD.calendarEvent " +
                    "           inner join totalSecondsParticipantsCepD.email.domain.organizations as organizationTotalSeconds" +
                    "       where organizerTotalSecondsCepD.email = organizerCalendarEventParticipantDao.email" +
                    "           and organizerTotalSecondsCepD.organizer = true" +
                    "           and organizerTotalSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and organizationTotalSeconds.id = :tenantId" +
                    "           and exists (" +
                    "               select count(distinct domain.name)" +
                    "               from CalendarEventDao innerCalendarEvent" +
                    "               inner join innerCalendarEvent.participants innerParticipants" +
                    "               inner join innerParticipants.email.domain domain" +
                    "               where innerCalendarEvent = organizerTotalSecondsCepD.calendarEvent" +
                    "               group by innerCalendarEvent" +
                    "               having count(distinct domain.name) = 1" +
                    "           )" +
                    "   ), 0) as internalMeetingSeconds," +
                    "   (" +
                    "       select count(organizerMeetSecondsCepD.id) " +
                    "       from CalendarEventParticipantDao organizerMeetSecondsCepD " +
                    "       where organizerMeetSecondsCepD.email = organizerCalendarEventParticipantDao.email " +
                    "           and organizerMeetSecondsCepD.organizer = true " +
                    "           and organizerMeetSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and exists (" +
                    "               select count(distinct domain.name)" +
                    "               from CalendarEventDao innerCalendarEvent" +
                    "               inner join innerCalendarEvent.participants innerParticipants" +
                    "               inner join innerParticipants.email.domain domain" +
                    "               where innerCalendarEvent = organizerMeetSecondsCepD.calendarEvent" +
                    "               group by innerCalendarEvent" +
                    "               having count(distinct domain.name) > 1" +
                    "           ) " +
                    "   ) as externalMeetingCount," +
                    "   COALESCE(" +
                    "   (" +
                    "       select" +
                    "           sum(" +
                    "                   timestampdiff(" +
                    "                       second," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.start," +
                    "                       totalSecondsParticipantsCepD.calendarEvent.end" +
                    "                   )" +
                    "           )" +
                    "       from CalendarEventParticipantDao organizerTotalSecondsCepD" +
                    "           left join CalendarEventParticipantDao totalSecondsParticipantsCepD " +
                    "               on organizerTotalSecondsCepD.calendarEvent = totalSecondsParticipantsCepD.calendarEvent " +
                    "           inner join totalSecondsParticipantsCepD.email.domain.organizations as organizationTotalSeconds" +
                    "       where organizerTotalSecondsCepD.email = organizerCalendarEventParticipantDao.email" +
                    "           and organizerTotalSecondsCepD.organizer = true" +
                    "           and organizerTotalSecondsCepD.calendarEvent.start between :sDate and :eDate" +
                    "           and organizationTotalSeconds.id = :tenantId" +
                    "   ), 0) as externalMeetingSeconds," +
                    "   concat(organizerEmailDao.user, '@', organizerDomainDao.name) as organizerEmail, " +
                    "   concat(peopleDao.firstName) as organizerFirstName, " +
                    "   concat(peopleDao.lastName) as organizerLastName, " +
                    "   concat(peopleDao.avatarUrl) as organizerAvatarUrl, " +
                    "   CONCAT(SUBSTR(hex(peopleDao.id), 1, 8), '-', SUBSTR(hex(peopleDao.id), 9, 4), '-', " +
                    "        SUBSTR(hex(peopleDao.id), 13, 4), '-', SUBSTR(hex(peopleDao.id), 17, 4), '-', SUBSTR(hex(peopleDao.id), 21, 12)) as organizerId " +
                    "from CalendarEventParticipantDao organizerCalendarEventParticipantDao " +
                    "   inner join organizerCalendarEventParticipantDao.email organizerEmailDao" +
                    "   inner join organizerEmailDao.domain organizerDomainDao" +
                    "   inner join organizerCalendarEventParticipantDao.calendarEvent organizerCalendarEvent " +
                    "   inner join organizerDomainDao.organizations tenant " +
                    "   inner join organizerEmailDao.people peopleDao " +
                    "   inner join peopleDao.employee as employee " +
                    "   inner join employee.department as departments " +
                    "where tenant.id = :tenantId" +
                    "   and organizerCalendarEventParticipantDao.organizer = true " +
                    "   and organizerCalendarEventParticipantDao.calendarEvent.start between :sDate and :eDate " +
                    "   and departments.name = :department " +
                    "group by organizerCalendarEventParticipantDao.email",
            countQuery = "select count(distinct cepd.email)" +
                    " from CalendarEventParticipantDao cepd" +
                    "   inner join cepd.calendarEvent ced" +
                    "   inner join cepd.email ed" +
                    "   inner join ed.people people " +
                    "   inner join ed.domain dd" +
                    "   inner join dd.organizations org " +
                    "   inner join people.employee as employee " +
                    "   inner join employee.department as departments " +
                    "where org.id = :tenantId" +
                    "   and ced.start between :sDate and :eDate" +
                    "   and cepd.organizer = true" +
                    "   and departments.name = :department "
    )
    Page<OrganizerStatisticsAggregateView> findAllDepartmentOrganizerStatistics(Instant sDate, Instant eDate, UUID tenantId, String department, Pageable pageable);

}
