package com.getkhaki.api.bff.domain.persistence;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;

import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;

public interface OrganizersStatisticsPersistenceInterface {
    List<OrganizerStatisticsDm> getOrganizersStatistics(Instant start, Instant end, OptionalInt count);
}
