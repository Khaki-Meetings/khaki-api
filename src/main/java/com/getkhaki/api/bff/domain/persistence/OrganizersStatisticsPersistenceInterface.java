package com.getkhaki.api.bff.domain.persistence;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;

public interface OrganizersStatisticsPersistenceInterface {
    Page<OrganizerStatisticsDm> getOrganizersStatistics(Instant start, Instant end, Pageable pageable);
}
