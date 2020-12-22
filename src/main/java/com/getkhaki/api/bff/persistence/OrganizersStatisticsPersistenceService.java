package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import com.getkhaki.api.bff.persistence.repositories.OrganizerStatisticsRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.List;
import java.util.OptionalInt;

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
    public Page<OrganizerStatisticsDm> getOrganizersStatistics(Instant start, Instant end, Pageable pageable) {
        Page<OrganizerStatisticsView> organizerStatisticsViewList = organizerStatisticsRepository
                .findAllOrganizerStatistics(start, end, sessionTenant.getTenantId(), pageable);

        return modelMapper.map(
                organizerStatisticsViewList,
                new TypeToken<Page<OrganizerStatisticsDm>>() {
                }.getType()
        );
    }


}
