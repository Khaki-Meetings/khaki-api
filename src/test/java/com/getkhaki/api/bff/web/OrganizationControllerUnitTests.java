package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import com.getkhaki.api.bff.domain.services.OrganizationService;
import com.getkhaki.api.bff.web.models.OrganizationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrganizationControllerUnitTests {
    private OrganizationController underTest;
    private OrganizationPersistenceInterface organizationPersistenceService;
    private OrganizationService organizationService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        organizationService = mock(OrganizationService.class);
        organizationPersistenceService = mock(OrganizationPersistenceInterface.class);
        modelMapper = mock(ModelMapper.class);
        underTest = new OrganizationController(this.organizationService, this.organizationPersistenceService, this.modelMapper);
    }

    @Test
    public void getOrganization() {
        var mockOrganizationDm = mock(OrganizationDm.class);
        var mockOrganizationDto = mock(OrganizationDto.class);

        when(this.organizationService.getOrganization())
                .thenReturn(mockOrganizationDm);

        when(this.modelMapper.map(mockOrganizationDm, OrganizationDto.class))
                .thenReturn(mockOrganizationDto);

        OrganizationDto response = this.underTest.getOrganization();

        assertSame(response, mockOrganizationDto);
    }

    @Test
    public void createOrganization() {
        var organizationDto = new OrganizationDto()
                .setName("ACME")
                .setAdminEmail("admin@acme.com");

        var mockDm = mock(OrganizationDm.class);
        var mockPersistedDm = mock(OrganizationDm.class);
        var mockDto = mock(OrganizationDto.class);

        when(this.modelMapper.map(organizationDto, OrganizationDm.class))
                .thenReturn(mockDm);

        when(this.organizationPersistenceService.upsertOrganization(mockDm))
                .thenReturn(mockPersistedDm);

        when(this.modelMapper.map(mockPersistedDm, OrganizationDto.class))
                .thenReturn(mockDto);

        var result = this.underTest.createOrganization(organizationDto);

        assertSame(mockDto, result);
    }
}
