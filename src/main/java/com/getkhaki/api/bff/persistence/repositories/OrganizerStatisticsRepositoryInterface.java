package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventParticipantDao;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizerStatisticsRepositoryInterface extends JpaRepository<CalendarEventParticipantDao, UUID> {
    @Query(
            "select " +
                    "   (" +
                    "       select count(organizerMeetHoursCepD.id) " +
                    "       from CalendarEventParticipantDao organizerMeetHoursCepD " +
                    "       where organizerMeetHoursCepD.email = organizerCalendarEventParticipantDao.email " +
                    "           and organizerMeetHoursCepD.organizer = true " +
                    "           and organizerMeetHoursCepD.calendarEvent.start between :sDate and :eDate" +
                    "   ) as totalMeetingCount," +
                    "   (" +
                    "       select" +
                    "           sum(" +
                    "               timestampdiff(" +
                    "                   hour," +
                    "                   totalHoursParticipantsCepD.calendarEvent.start," +
                    "                   totalHoursParticipantsCepD.calendarEvent.end" +
                    "               )" +
                    "           )" +
                    "       from CalendarEventParticipantDao organizerTotalHoursCepD" +
                    "           left join CalendarEventParticipantDao totalHoursParticipantsCepD " +
                    "               on organizerTotalHoursCepD.calendarEvent = totalHoursParticipantsCepD.calendarEvent " +
                    "       where organizerTotalHoursCepD.email = organizerCalendarEventParticipantDao.email" +
                    "           and organizerTotalHoursCepD.organizer = true" +
                    "           and organizerTotalHoursCepD.calendarEvent.start between :sDate and :eDate" +
                    "   ) as totalHours," +
                    "   (" +
                    "       (" +
                    "           select" +
                    "               sum(" +
                    "                   timestampdiff(" +
                    "                       hour," +
                    "                       participantsTotalHourlyCepD.calendarEvent.start," +
                    "                       participantsTotalHourlyCepD.calendarEvent.end" +
                    "                   )" +
                    "               )" +
                    "           from CalendarEventParticipantDao organizerTotalHours2CepD" +
                    "               left join CalendarEventParticipantDao participantsTotalHourlyCepD " +
                    "                   on organizerTotalHours2CepD.calendarEvent = participantsTotalHourlyCepD.calendarEvent " +
                    "           where organizerTotalHours2CepD.email = organizerCalendarEventParticipantDao.email" +
                    "               and organizerTotalHours2CepD.organizer = true" +
                    "               and organizerTotalHours2CepD.calendarEvent.start between :sDate and :eDate" +
                    "       )" +
                    "       * " +
                    "       ((" +
                    "           select sum(participantsSalaryGroupD.salary)" +
                    "           from CalendarEventParticipantDao organizerTotalHourlyCostCepD" +
                    "               left join CalendarEventParticipantDao participantsTotalHourlyCostCepD " +
                    "                   on organizerTotalHourlyCostCepD.calendarEvent = participantsTotalHourlyCostCepD.calendarEvent " +
                    "               inner join participantsTotalHourlyCostCepD.email participantsTotalHourlyCostEmailD" +
                    "               inner join participantsTotalHourlyCostEmailD.people participantsTotalHourlyCostPeople" +
                    "               inner join participantsTotalHourlyCostPeople.employee participantsTotalHourlyCostEmployeeD" +
                    "               inner join participantsTotalHourlyCostEmployeeD.salaryGroup participantsSalaryGroupD" +
                    "           where organizerTotalHourlyCostCepD.email = organizerCalendarEventParticipantDao.email" +
                    "               and organizerTotalHourlyCostCepD.organizer = true" +
                    "               and organizerTotalHourlyCostCepD.calendarEvent.start between :sDate and :eDate" +
                    "       ) / 2000)" +
                    "   ) as totalCost," +
                    "   concat(organizerEmailDao.user, '@', organizerDomainDao.name) as organizerEmail " +
                    "from CalendarEventParticipantDao organizerCalendarEventParticipantDao " +
                    "   inner join organizerCalendarEventParticipantDao.email organizerEmailDao" +
                    "   inner join organizerEmailDao.domain organizerDomainDao" +
                    "   inner join organizerCalendarEventParticipantDao.calendarEvent organizerCalendarEvent " +
                    "   inner join organizerDomainDao.organizations tenant " +
                    "where :organizationId is not null" +
                    "   and organizerCalendarEventParticipantDao.organizer = true " +
                    "   and organizerCalendarEventParticipantDao.calendarEvent.start between :sDate and :eDate " +
                    "group by organizerCalendarEventParticipantDao.email"
    )
    List<OrganizerStatisticsView> findAllOrganizerStatistics(Instant sDate, Instant eDate, UUID organizationId);
}
