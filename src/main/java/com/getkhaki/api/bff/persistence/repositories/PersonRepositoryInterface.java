package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepositoryInterface extends JpaRepository<PersonDao, UUID> {
    PersonDao findDistinctByEmailsUserAndEmailsDomainName(String userName, String domainName);

    Optional<PersonDao> findDistinctByEmails(EmailDao emailDao);

    @Query(
            value = "select id as id, " +
                    "       first_name as firstName, " +
                    "       last_name as lastName " +
                    "  from person_dao pd " +
                    " where exists ( " +
                    "   select 'x' " +
                    "   from calendar_event_participant_dao cepd," +
                    "        email_dao_people edp " +
                    "   where cepd.email_id = edp.emails_id " +
                    "   and edp.people_id = pd.id " +
                    "   and cepd.calendar_event_id = unhex(:calendarEventId) )",
        nativeQuery = true
    )
    List<PersonDao> findDistinctByCalendarEvent(String calendarEventId);

}
