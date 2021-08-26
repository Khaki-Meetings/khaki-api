package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.FlagDe;
import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.repositories.DomainRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrganizationPersistenceService implements OrganizationPersistenceInterface {
    private final OrganizationRepositoryInterface organizationRepository;
    private final DomainRepositoryInterface domainRepository;
    private final EmailDaoService emailDaoService;
    private final ModelMapper modelMapper;
    private final SessionTenant sessionTenant;

    @Autowired
    public OrganizationPersistenceService(
            OrganizationRepositoryInterface organizationRepository,
            DomainRepositoryInterface domainRepository, EmailDaoService emailDaoService,
            ModelMapper modelMapper,
            SessionTenant sessionTenant
    ) {
        this.organizationRepository = organizationRepository;
        this.domainRepository = domainRepository;
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

    @Override
    public List<OrganizationDm> getOrganizationsByAdminEmail(String email) {
        String[] emailParts = email.split("@");
        assert emailParts.length == 2 : "Email could not be split";

        List<OrganizationDao> organizationDaos = new ArrayList<OrganizationDao>();

        Optional<DomainDao> domainDaoOptional = this.domainRepository.findDistinctByName(emailParts[1]);
        if (domainDaoOptional.isPresent()) {
            DomainDao domainDao = domainDaoOptional.get();
            Optional<EmailDao> emailDaoOptional = this.emailDaoService.findDistinctByUserAndDomainId(emailParts[0], domainDao.getId());
            if (emailDaoOptional.isPresent()) {
                EmailDao emailDao = emailDaoOptional.get();
                organizationDaos.addAll(this.organizationRepository.findByAdminEmailId(emailDao.getId()));
            }
        }

        List<OrganizationDm> organizationDms = organizationDaos.stream()
                .map(org -> modelMapper.map(org, OrganizationDm.class))
                .collect(Collectors.toList());

        return organizationDms;

    }
}
