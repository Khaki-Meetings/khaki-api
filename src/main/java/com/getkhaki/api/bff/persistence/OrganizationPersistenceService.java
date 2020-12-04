package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import com.getkhaki.api.bff.security.AuthenticationFacade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class OrganizationPersistenceService implements OrganizationPersistenceInterface {
    private final OrganizationRepositoryInterface organizationRepository;
    private final AuthenticationFacade authenticationFacade;
    private final ModelMapper modelMapper;

    @Autowired
    public OrganizationPersistenceService(
            OrganizationRepositoryInterface organizationRepository,
            AuthenticationFacade authenticationFacade,
            ModelMapper modelMapper
    ) {
        this.organizationRepository = organizationRepository;
        this.authenticationFacade = authenticationFacade;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrganizationDm getOrganization() {
        Authentication authentication = this.authenticationFacade.getAuthentication();
        OidcUser principal = (OidcUser) authentication.getPrincipal();

        return this.modelMapper.map(
                this.organizationRepository.findById(
                        principal.getClaim("tenantId")
                ), OrganizationDm.class
        );
    }
}
