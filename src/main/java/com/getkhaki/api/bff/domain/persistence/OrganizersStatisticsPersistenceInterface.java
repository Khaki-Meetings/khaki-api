package com.getkhaki.api.bff.domain.persistence;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.OptionalInt;

public interface OrganizersStatisticsPersistenceInterface {
    List<OrganizerStatisticsDm> getOrganizersStatistics(ZonedDateTime start, ZonedDateTime end, OptionalInt count);
}
