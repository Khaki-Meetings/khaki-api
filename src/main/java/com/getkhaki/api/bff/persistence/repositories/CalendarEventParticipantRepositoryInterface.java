package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventParticipantDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalendarEventParticipantRepositoryInterface extends JpaRepository<CalendarEventParticipantDao, UUID> {
    Optional<CalendarEventParticipantDao> findDistinctByCalendarEvent_IdAndEmail_Id(UUID calendarEventId, UUID emailId);
}
