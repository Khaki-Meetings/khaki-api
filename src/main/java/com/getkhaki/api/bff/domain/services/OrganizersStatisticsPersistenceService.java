package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import org.springframework.stereotype.Service;

@Service
public class OrganizersStatisticsPersistenceService implements OrganizersStatisticsPersistenceInterface {
    @Override
    public OrganizersStatisticsDm getOrganizerStatistics(String email) {
        return null;
    }
}
