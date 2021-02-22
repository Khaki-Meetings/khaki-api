package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.OrganizationDm;

import java.util.Set;

public interface OrganizationPersistenceInterface {
    OrganizationDm getOrganization();
    Set<OrganizationDm> getImportEnabledOrganizations();
    OrganizationDm upsertOrganization(OrganizationDm organizationDm);
}
