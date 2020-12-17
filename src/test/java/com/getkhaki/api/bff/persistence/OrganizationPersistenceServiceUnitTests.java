package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class OrganizationPersistenceServiceUnitTests {
    @Mock
    private OrganizationRepositoryInterface organizationRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private SessionTenant sessionTenant;

    @InjectMocks
    private OrganizationPersistenceService underTest;

    @Test
    public void success_getImportEnabledOrganizations() {
        val orgs = underTest.getImportEnabledOrganizations();
    }

}
