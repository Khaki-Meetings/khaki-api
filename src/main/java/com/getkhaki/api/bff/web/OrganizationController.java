package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import com.getkhaki.api.bff.domain.services.OrganizationService;
import com.getkhaki.api.bff.web.models.OrganizationDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/organizations")
@RestController
@CrossOrigin(origins = "*")
public class OrganizationController {
    private final OrganizationService organizationService;
    private final OrganizationPersistenceInterface organizationPersistenceService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrganizationController(
            OrganizationService organizationService,
            OrganizationPersistenceInterface organizationPersistenceService,
            ModelMapper modelMapper
    ) {
        this.organizationService = organizationService;
        this.organizationPersistenceService = organizationPersistenceService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public OrganizationDto getOrganization() {
        OrganizationDm organizationDm = organizationService.getOrganization();
        OrganizationDto organizationDto = this.modelMapper.map(organizationDm, OrganizationDto.class);

        return organizationDto;
    }

    @PostMapping
    public OrganizationDto createOrganization(@RequestBody OrganizationDto organizationDto) {
        var organizationDm = this.modelMapper.map(organizationDto, OrganizationDm.class);
        OrganizationDm updatedOrganizationDm = this.organizationPersistenceService.upsertOrganization(organizationDm);
        var mappedOrganizationDto = this.modelMapper.map(updatedOrganizationDm, OrganizationDto.class);

        return mappedOrganizationDto;
    }
}
