package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {
    private final OrganizationPersistenceInterface organizationPersistenceInterface;

    @Autowired
    public OrganizationService(OrganizationPersistenceInterface organizationPersistenceInterface) {
        this.organizationPersistenceInterface = organizationPersistenceInterface;
    }

    public OrganizationDm getOrganization() {
        return this.organizationPersistenceInterface.getOrganization();
    }
}
