package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.OrganizersStatisticsRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class OrganizersStatisticsPersistenceService implements OrganizersStatisticsPersistenceInterface {

    private final ModelMapper modelMapper;

    private OrganizersStatisticsRepositoryInterface organizersStatisticsRepositoryInterface;

    public OrganizersStatisticsPersistenceService(OrganizersStatisticsRepositoryInterface organizersStatisticsRepositoryInterface,ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.organizersStatisticsRepositoryInterface=organizersStatisticsRepositoryInterface;
    }

    @Override
    public List<OrganizerStatisticsDm> getOrganizerStatistics(ZonedDateTime start, ZonedDateTime end, int count) {
        return modelMapper.map(
                organizersStatisticsRepositoryInterface.findOrganizersStatistics(start, end, count),
                new TypeToken<List<OrganizerStatisticsDm>>() {}.getType()
        );
    }



}
