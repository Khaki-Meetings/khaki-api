package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import com.getkhaki.api.bff.persistence.repositories.OrganizerStatisticsRepositoryInterface;
import com.getkhaki.api.bff.security.AuthenticationFacade;
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

    private final OrganizerStatisticsRepositoryInterface organizerStatisticsRepository;

    private final SessionTenant sessionTenant;

    public OrganizersStatisticsPersistenceService(
            OrganizerStatisticsRepositoryInterface organizerStatisticsRepository,
            ModelMapper modelMapper,
            SessionTenant sessionTenant
    ) {
        this.sessionTenant = sessionTenant;
        this.modelMapper = modelMapper;
        this.organizerStatisticsRepository = organizerStatisticsRepository;
    }

    @Override
    public List<OrganizerStatisticsDm> getOrganizersStatistics(Instant start, Instant end, OptionalInt count) {
        List<OrganizerStatisticsView> organizerStatisticsViewList = organizerStatisticsRepository
                .findAllOrganizerStatistics(start, end, sessionTenant.getTenantId());

        return modelMapper.map(
                organizerStatisticsViewList,
                new TypeToken<List<OrganizerStatisticsDm>>() {
                }.getType()
        );
    }


}
