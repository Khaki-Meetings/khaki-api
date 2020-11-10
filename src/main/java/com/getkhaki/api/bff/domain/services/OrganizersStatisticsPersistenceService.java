package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizersStatisticsRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrganizersStatisticsPersistenceService implements OrganizersStatisticsPersistenceInterface {

    private final ModelMapper modelMapper;

    @Autowired
    OrganizersStatisticsRepositoryInterface organizersStatisticsRepositoryInterface;


    public OrganizersStatisticsPersistenceService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public OrganizersStatisticsDm getOrganizerStatistics(String email) {
        return modelMapper.map( organizersStatisticsRepositoryInterface.findOrganizerStatisticsByEmail(email),OrganizersStatisticsDm.class);
    }



}
