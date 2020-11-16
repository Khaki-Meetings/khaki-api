package com.getkhaki.api.bff.persistence.repositories;
import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.models.OrganizerStatisticsDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface OrganizersStatisticsRepositoryInterface extends JpaRepository<CalendarEventDao, UUID> {
//    @Query("")
//    List<OrganizerStatisticsDao> findOrganizersStatistics(ZonedDateTime start, ZonedDateTime end, int count);
}
