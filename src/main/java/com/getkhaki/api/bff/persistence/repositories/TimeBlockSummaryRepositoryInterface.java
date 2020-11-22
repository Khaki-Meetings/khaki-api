package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;
import com.getkhaki.api.bff.persistence.models.TimeBlockSummaryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface TimeBlockSummaryRepositoryInterface extends JpaRepository<CalendarEventDao, UUID> {
//    @Query("")
//    TimeBlockSummaryDao findTimeBlockSummaryInRange(ZonedDateTime start, ZonedDateTime end);
//    @Query("")
//    List<TimeBlockSummaryDao> findTimeBlockSummaryInRangeWithInterval(ZonedDateTime start, ZonedDateTime end, IntervalEnumDao interval);
}
