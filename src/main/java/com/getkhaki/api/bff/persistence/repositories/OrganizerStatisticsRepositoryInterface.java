package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventParticipantDao;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
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
                    "   (" +
                    "       (" +
                    "           select" +
                    "               sum(" +
                    "                      timestampdiff(" +
                    "                          second," +
                    "                          participantsTotalSecondsCepD.calendarEvent.start," +
                    "                          participantsTotalSecondsCepD.calendarEvent.end" +
                    "                      )" +
                    "               )" +
                    "           from CalendarEventParticipantDao organizerTotalSeconds2CepD" +
                    "               left join CalendarEventParticipantDao participantsTotalSecondsCepD " +
                    "                   on organizerTotalSeconds2CepD.calendarEvent = participantsTotalSecondsCepD.calendarEvent " +
                    "               inner join participantsTotalSecondsCepD.email.domain.organizations as organizationTotalSeconds" +
                    "           where organizerTotalSeconds2CepD.email = organizerCalendarEventParticipantDao.email" +
                    "               and organizerTotalSeconds2CepD.organizer = true" +
                    "               and organizerTotalSeconds2CepD.calendarEvent.start between :sDate and :eDate" +
                    "               and organizationTotalSeconds.id = :tenantId" +
                    "       )" +
                    "       * " +
                    "       ((" +
                    "           select sum(participantsSalaryGroupD.salary)" +
                    "           from CalendarEventParticipantDao organizerTotalSecondsCostCepD" +
                    "               left join CalendarEventParticipantDao participantsTotalSecondsCostCepD " +
                    "                   on organizerTotalSecondsCostCepD.calendarEvent = participantsTotalSecondsCostCepD.calendarEvent " +
                    "               inner join participantsTotalSecondsCostCepD.email participantsTotalSecondsCostEmailD" +
                    "               inner join participantsTotalSecondsCostEmailD.people participantsTotalSecondsCostPeople" +
                    "               inner join participantsTotalSecondsCostPeople.employee participantsTotalSecondsCostEmployeeD" +
                    "               inner join participantsTotalSecondsCostEmployeeD.salaryGroup participantsSalaryGroupD" +
                    "           where organizerTotalSecondsCostCepD.email = organizerCalendarEventParticipantDao.email" +
                    "               and organizerTotalSecondsCostCepD.organizer = true" +
                    "               and organizerTotalSecondsCostCepD.calendarEvent.start between :sDate and :eDate" +
                    "       ) / 7200000)" + // seconds in a work year
                    "   ) as totalCost," +
                    "   concat(organizerEmailDao.user, '@', organizerDomainDao.name) as organizerEmail, " +
                    "   concat(peopleDao.firstName) as organizerFirstName, " +
                    "   concat(peopleDao.lastName) as organizerLastName " +
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


//    @Query(
//            value = "select " +
//                    "   (" +
//                    "       select count(organizerMeetSecondsCepD.id) " +
//                    "       from CalendarEventParticipantDao organizerMeetSecondsCepD " +
//                    "       where organizerMeetSecondsCepD.email = organizerCalendarEventParticipantDao.email " +
//                    "           and organizerMeetSecondsCepD.organizer = true " +
//                    "           and organizerMeetSecondsCepD.calendarEvent.start between :sDate and :eDate" +
//                    "   ) as totalMeetings," +
//                    "   (" +
//                    "       select" +
//                    "           sum(" +
//                    "                   timestampdiff(" +
//                    "                       second," +
//                    "                       totalSecondsParticipantsCepD.calendarEvent.start," +
//                    "                       totalSecondsParticipantsCepD.calendarEvent.end" +
//                    "                   )" +
//                    "           )" +
//                    "       from CalendarEventParticipantDao organizerTotalSecondsCepD" +
//                    "           left join CalendarEventParticipantDao totalSecondsParticipantsCepD " +
//                    "               on organizerTotalSecondsCepD.calendarEvent = totalSecondsParticipantsCepD.calendarEvent " +
//                    "           inner join totalSecondsParticipantsCepD.email.domain.organizations as organizationTotalSeconds" +
//                    "       where organizerTotalSecondsCepD.email = organizerCalendarEventParticipantDao.email" +
//                    "           and organizerTotalSecondsCepD.organizer = true" +
//                    "           and organizerTotalSecondsCepD.calendarEvent.start between :sDate and :eDate" +
//                    "           and organizationTotalSeconds.id = :tenantId" +
//                    "   ) as totalSeconds," +
//                    "   (" +
//                    "       (" +
//                    "           select" +
//                    "               sum(" +
//                    "                      timestampdiff(" +
//                    "                          second," +
//                    "                          participantsTotalSecondsCepD.calendarEvent.start," +
//                    "                          participantsTotalSecondsCepD.calendarEvent.end" +
//                    "                      )" +
//                    "               )" +
//                    "           from CalendarEventParticipantDao organizerTotalSeconds2CepD" +
//                    "               left join CalendarEventParticipantDao participantsTotalSecondsCepD " +
//                    "                   on organizerTotalSeconds2CepD.calendarEvent = participantsTotalSecondsCepD.calendarEvent " +
//                    "               inner join participantsTotalSecondsCepD.email.domain.organizations as organizationTotalSeconds" +
//                    "           where organizerTotalSeconds2CepD.email = organizerCalendarEventParticipantDao.email" +
//                    "               and organizerTotalSeconds2CepD.organizer = true" +
//                    "               and organizerTotalSeconds2CepD.calendarEvent.start between :sDate and :eDate" +
//                    "               and organizationTotalSeconds.id = :tenantId" +
//                    "       )" +
//                    "       * " +
//                    "       ((" +
//                    "           select sum(participantsSalaryGroupD.salary)" +
//                    "           from CalendarEventParticipantDao organizerTotalSecondsCostCepD" +
//                    "               left join CalendarEventParticipantDao participantsTotalSecondsCostCepD " +
//                    "                   on organizerTotalSecondsCostCepD.calendarEvent = participantsTotalSecondsCostCepD.calendarEvent " +
//                    "               inner join participantsTotalSecondsCostCepD.email participantsTotalSecondsCostEmailD" +
//                    "               inner join participantsTotalSecondsCostEmailD.people participantsTotalSecondsCostPeople" +
//                    "               inner join participantsTotalSecondsCostPeople.employee participantsTotalSecondsCostEmployeeD" +
//                    "               inner join participantsTotalSecondsCostEmployeeD.salaryGroup participantsSalaryGroupD" +
//                    "           where organizerTotalSecondsCostCepD.email = organizerCalendarEventParticipantDao.email" +
//                    "               and organizerTotalSecondsCostCepD.organizer = true" +
//                    "               and organizerTotalSecondsCostCepD.calendarEvent.start between :sDate and :eDate" +
//                    "       ) / 7200000)" + // seconds in a work year
//                    "   ) as totalCost," +
//                    "   concat(organizerEmailDao.user, '@', organizerDomainDao.name) as organizerEmail, " +
//                    "   concat(peopleDao.firstName) as organizerFirstName, " +
//                    "   concat(peopleDao.lastName) as organizerLastName " +
//                    "from CalendarEventParticipantDao organizerCalendarEventParticipantDao " +
//                    "   inner join organizerCalendarEventParticipantDao.email organizerEmailDao" +
//                    "   inner join organizerEmailDao.domain organizerDomainDao" +
//                    "   inner join organizerCalendarEventParticipantDao.calendarEvent organizerCalendarEvent " +
//                    "   inner join organizerDomainDao.organizations tenant " +
//                    "   inner join organizerEmailDao.people peopleDao " +
//                    "where tenant.id = :tenantId" +
//                    "   and organizerCalendarEventParticipantDao.organizer = true " +
//                    "   and organizerCalendarEventParticipantDao.calendarEvent.start between :sDate and :eDate " +
//                    "group by organizerCalendarEventParticipantDao.email",
//            countQuery = "select count(distinct cepd.email)" +
//                    " from CalendarEventParticipantDao cepd" +
//                    "   inner join cepd.calendarEvent ced" +
//                    "   inner join cepd.email ed" +
//                    "   inner join ed.people" +
//                    "   inner join ed.domain dd" +
//                    "   inner join dd.organizations org " +
//                    "where org.id = :tenantId" +
//                    "   and ced.start between :sDate and :eDate" +
//                    "   and cepd.organizer = true"
//    )
//    Page<OrganizerStatisticsView> findAllOrganizerStatistics(Instant sDate, Instant eDate, UUID tenantId, Pageable pageable);
}
