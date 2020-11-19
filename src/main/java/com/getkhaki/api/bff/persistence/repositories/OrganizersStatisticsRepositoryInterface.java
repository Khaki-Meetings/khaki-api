package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.OrganizerStatisticsDao;
import com.getkhaki.api.bff.persistence.models.OrganizerStatisticsDaoInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizersStatisticsRepositoryInterface extends JpaRepository<OrganizerStatisticsDao, UUID> {
    //    @Query(
//            value = "select\n" +
//                    "           ced.id,\n" +
//                    "           pd.first_name,\n" +
//                    "           pd.last_name,\n" +
//                    "           concat(ed.user, '@', domain.name) as email,\n" +
//                    "           count(ced.id)                     as totalMeetings,\n" +
//                    "           (\n" +
//                    "               select count(*)\n" +
//                    "               from calendar_event_participant_dao cepd2\n" +
//                    "               where cepd2.calendar_event_id = ced.id\n" +
//                    "           )                                 as participantCount,\n" +
//                    "           sum((\n" +
//                    "               select timestampdiff(hour, start, end) * participantCount\n" +
//                    "               from calendar_event_dao ced2\n" +
//                    "               where ced2.id = ced.id\n" +
//                    "           ))                                as totalHours,\n" +
//                    "           sum((\n" +
//                    "               select sum(sgd.salary)\n" +
//                    "               from calendar_event_dao ced3\n" +
//                    "                        left join calendar_event_participant_dao cepd on ced3.id = cepd.calendar_event_id\n" +
//                    "                        left join participant_type_dao ptd on ptd.id = cepd.participant_type_id\n" +
//                    "                        left join email_dao ed on cepd.email_id = ed.id\n" +
//                    "                        left join email_dao_people edp on ed.id = edp.emails_id\n" +
//                    "                        left join person_dao pd on edp.people_id = pd.id\n" +
//                    "                        left join employee_dao e on pd.id = e.person_id\n" +
//                    "                        left join salary_group_dao sgd on e.salary_group_id = sgd.id\n" +
//                    "               where ced3.id = ced.id\n" +
//                    "           ))                                as totalCost\n" +
//                    "    from calendar_event_dao ced\n" +
//                    "             left join calendar_event_participant_dao cepd on ced.id = cepd.calendar_event_id\n" +
//                    "             left join participant_type_dao ptd on cepd.participant_type_id = ptd.id\n" +
//                    "             left join email_dao ed on cepd.email_id = ed.id\n" +
//                    "             left join domain_dao domain on ed.domain_id = domain.id\n" +
//                    "             left join email_dao_people edp on ed.id = edp.emails_id\n" +
//                    "             left join person_dao pd on edp.people_id = pd.id\n" +
//                    "             left join employee_dao e on pd.id = e.person_id\n" +
//                    "             left join department_dao dd on e.department_id = dd.id\n" +
//                    "             left join organization_dao od on dd.organization_id = od.id\n" +
//                    "    where od.id = hextoraw(replace(:orgId, '-', ''))\n" +
//                    "      and ptd.name = 'Organizer'\n" +
//                    "    group by ed.id",
//            nativeQuery = true
//    )
    @Query(
            "select person.firstName as firstName from CalendarEventDao as calendarEvent " +
                    "left join calendarEvent.participants as participants " +
                    "left join participants.email as email " +
                    "left join email.people as person"
    )
    List<OrganizerStatisticsDaoInterface> findOrganizersStatistics(String orgId);
}
