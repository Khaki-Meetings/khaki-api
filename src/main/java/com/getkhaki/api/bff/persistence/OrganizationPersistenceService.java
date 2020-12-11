package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrganizationPersistenceService implements OrganizationPersistenceInterface {
    private final OrganizationRepositoryInterface organizationRepository;
    private final ModelMapper modelMapper;
    private final SessionTenant sessionTenant;

    @Autowired
    public OrganizationPersistenceService(
            OrganizationRepositoryInterface organizationRepository,
            ModelMapper modelMapper,
            SessionTenant sessionTenant
    ) {
        this.organizationRepository = organizationRepository;
        this.modelMapper = modelMapper;
        this.sessionTenant = sessionTenant;
    }

    @Override
    public OrganizationDm getOrganization() {
        UUID bla = sessionTenant.getTenantId();
        OrganizationDao organizationDao = this.organizationRepository.findById(sessionTenant.getTenantId())
                .orElseThrow();
        OrganizationDm organizationDm = modelMapper.map(organizationDao, OrganizationDm.class);
        return organizationDm;
    }
}
