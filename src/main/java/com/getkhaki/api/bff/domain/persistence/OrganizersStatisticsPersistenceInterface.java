package com.getkhaki.api.bff.domain.persistence;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;

import java.time.ZonedDateTime;
import java.util.List;

public interface OrganizersStatisticsPersistenceInterface {
    List<OrganizerStatisticsDm> getOrganizerStatistics(ZonedDateTime start, ZonedDateTime end, int count);
}
