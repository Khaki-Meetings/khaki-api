package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.OrganizerStatisticsRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.OptionalInt;

@Service
public class OrganizersStatisticsPersistenceService implements OrganizersStatisticsPersistenceInterface {

    private final ModelMapper modelMapper;

    private OrganizerStatisticsRepositoryInterface organizerStatisticsRepository;

    public OrganizersStatisticsPersistenceService(
            OrganizerStatisticsRepositoryInterface organizerStatisticsRepository,
            ModelMapper modelMapper
    ) {
        this.modelMapper = modelMapper;
        this.organizerStatisticsRepository = organizerStatisticsRepository;
    }

    @Override
    public List<OrganizerStatisticsDm> getOrganizersStatistics(ZonedDateTime start, ZonedDateTime end, OptionalInt count) {
//        List<OrganizerStatisticsViewDao> organizerStatisticsDaoList = organizerStatisticsRepository.findAll();
//        return modelMapper.map(
//                organizerStatisticsDaoList,
//                new TypeToken<List<OrganizerStatisticsDm>>() {
//                }.getType()
//        );
        return null;
    }


}
