package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.FlagDe;
import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrganizationPersistenceService implements OrganizationPersistenceInterface {
    private final OrganizationRepositoryInterface organizationRepository;
    private final EmailDaoService emailDaoService;
    private final ModelMapper modelMapper;
    private final SessionTenant sessionTenant;

    @Autowired
    public OrganizationPersistenceService(
            OrganizationRepositoryInterface organizationRepository,
            EmailDaoService emailDaoService,
            ModelMapper modelMapper,
            SessionTenant sessionTenant
    ) {
        this.organizationRepository = organizationRepository;
        this.emailDaoService = emailDaoService;
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

    @Override
    public OrganizationDm upsertOrganization(OrganizationDm organizationDm) {
        EmailDao adminEmail = this.emailDaoService.upsertByEmailString(organizationDm.getAdminEmail());

        var organizationDao = this.modelMapper.map(organizationDm, OrganizationDao.class);

        this.organizationRepository.findDistinctByName(organizationDao.getName())
                .ifPresentOrElse(foundDao -> {
                    organizationDao
                            .setAdminEmail(adminEmail)
                            .setDomains(
                                    Stream.of(
                                            foundDao.getDomains(),
                                            organizationDao.getDomains(),
                                            List.of(adminEmail.getDomain())
                                    )
                                            .flatMap(Collection::stream)
                                            .distinct()
                                            .map(domainDao -> domainDao.setOrganization(organizationDao))
                                            .collect(Collectors.toList())
                            )
                            .setId(foundDao.getId());
                }, () -> {
                    organizationDao
                            .setAdminEmail(adminEmail)
                            .setDomains(List.of(adminEmail.getDomain()
                                    .setOrganization(organizationDao)));
                });

        OrganizationDao result = this.organizationRepository.save(organizationDao);

        var mappedOrganizationDm = this.modelMapper.map(result, OrganizationDm.class);

        return mappedOrganizationDm;
    }
}
