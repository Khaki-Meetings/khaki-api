package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.OrganizersStatisticsRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class OrganizersStatisticsPersistenceService implements OrganizersStatisticsPersistenceInterface {

    private final ModelMapper modelMapper;

    private OrganizersStatisticsRepositoryInterface organizersStatisticsRepositoryInterface;


    public OrganizersStatisticsPersistenceService(OrganizersStatisticsRepositoryInterface organizersStatisticsRepositoryInterface,ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.organizersStatisticsRepositoryInterface=organizersStatisticsRepositoryInterface;
    }

    @Override
    public OrganizersStatisticsDm getOrganizerStatistics(String email) {
        return modelMapper.map( organizersStatisticsRepositoryInterface.findOrganizerStatisticsByEmail(email),OrganizersStatisticsDm.class);
    }



}
