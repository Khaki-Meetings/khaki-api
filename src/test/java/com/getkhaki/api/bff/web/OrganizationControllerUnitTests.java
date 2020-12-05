package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
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

    private OrganizationService organizationService;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        organizationService = mock(OrganizationService.class);
        modelMapper = mock(ModelMapper.class);
        underTest = new OrganizationController(this.organizationService, this.modelMapper);
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
}
