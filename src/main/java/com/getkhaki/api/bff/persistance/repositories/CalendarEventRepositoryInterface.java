package com.getkhaki.api.bff.persistance.repositories;

import com.getkhaki.api.bff.persistance.models.CalendarEventDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CalendarEventRepositoryInterface extends JpaRepository<CalendarEventDao, UUID> {
}
