package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.FlagDe;
import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

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
        OrganizationDao organizationDao = this.organizationRepository.findById(sessionTenant.getTenantId())
                .orElseThrow();
        return modelMapper.map(organizationDao, OrganizationDm.class);
    }

    @Override
    @Transactional
    public Set<OrganizationDm> getImportEnabledOrganizations() {
        return organizationRepository
                .findAll()
                .stream()
                .filter(
                        organizationDao ->
                                organizationDao.getFlags().stream().noneMatch(flagDao -> flagDao.getName().equals(FlagDe.DisableImport.toString()))
                )
                .map(
                        organizationDao -> modelMapper.map(organizationDao, OrganizationDm.class)
                )
                .collect(Collectors.toSet());
    }
}
