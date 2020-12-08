package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.services.OrganizationService;
import com.getkhaki.api.bff.web.models.OrganizationDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/organizations")
@RestController
public class OrganizationController {
    private final OrganizationService organizationService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrganizationController(OrganizationService organizationService, ModelMapper modelMapper) {
        this.organizationService = organizationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public OrganizationDto getOrganization() {
        OrganizationDm organizationDm = organizationService.getOrganization();
        OrganizationDto organizationDto = this.modelMapper.map(organizationDm, OrganizationDto.class);
        return organizationDto;
    }
}
