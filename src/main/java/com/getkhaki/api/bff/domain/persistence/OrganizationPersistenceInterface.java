package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;

import java.util.List;
import java.util.Set;

public interface OrganizationPersistenceInterface {
    OrganizationDm getOrganization();
    Set<OrganizationDm> getImportEnabledOrganizations();
    OrganizationDm upsertOrganization(OrganizationDm organizationDm);
    List<OrganizationDm> getOrganizationsByAdminEmail(String email);
}
