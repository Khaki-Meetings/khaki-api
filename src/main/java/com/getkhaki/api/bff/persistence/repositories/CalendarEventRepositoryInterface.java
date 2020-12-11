package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CalendarEventRepositoryInterface extends JpaRepository<CalendarEventDao, UUID> {
    Optional<CalendarEventDao> findDistinctByGoogleCalendarId(String googleCalendarId);
}
