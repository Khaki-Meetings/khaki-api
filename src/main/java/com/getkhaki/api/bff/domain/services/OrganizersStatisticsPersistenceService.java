package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import com.getkhaki.api.bff.persistence.repositories.OrganizerStatisticsRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;
import java.util.UUID;

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
    public List<OrganizerStatisticsDm> getOrganizersStatistics(Instant start, Instant end, OptionalInt count) {
        List<OrganizerStatisticsView> organizerStatisticsViewList = organizerStatisticsRepository
                .findAllOrganizerStatistics(start, end, UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4"));
        return modelMapper.map(
                organizerStatisticsViewList,
                new TypeToken<List<OrganizerStatisticsDm>>() {
                }.getType()
        );
    }


}
