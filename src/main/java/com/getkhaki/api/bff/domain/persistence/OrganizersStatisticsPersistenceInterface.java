package com.getkhaki.api.bff.domain.persistence;
import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;

public interface OrganizersStatisticsPersistenceInterface {
    OrganizersStatisticsDm getOrganizerStatistics(String email);
}
