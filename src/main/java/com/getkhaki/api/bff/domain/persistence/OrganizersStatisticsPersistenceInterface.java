package com.getkhaki.api.bff.domain.persistence;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsAggregateDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.web.models.StatisticsFilterDte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;

public interface OrganizersStatisticsPersistenceInterface {
    Page<OrganizerStatisticsDm> getOrganizersStatistics(Instant start, Instant end, Pageable pageable, StatisticsFilterDe filterDe);

    Page<OrganizerStatisticsAggregateDm> getAggregateOrganizersStatistics(Instant start, Instant end, String department, Pageable pageable);
}
