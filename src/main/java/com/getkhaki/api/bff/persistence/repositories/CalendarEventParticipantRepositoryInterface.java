package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.models.CalendarEventParticipantDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CalendarEventParticipantRepositoryInterface extends JpaRepository<CalendarEventParticipantDao, UUID> {
    Optional<CalendarEventParticipantDao> findDistinctByCalendarEventAndEmail(CalendarEventDao calendarEvent, EmailDao email);

    Set<CalendarEventParticipantDao> findDistinctByCalendarEvent(CalendarEventDao calendarEvent);
}
