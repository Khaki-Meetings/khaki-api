package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsAggregateDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsAggregateView;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import com.getkhaki.api.bff.persistence.repositories.OrganizerStatisticsRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.time.Instant;

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
    public Page<OrganizerStatisticsDm> getOrganizersStatistics(
            Instant start,
            Instant end,
            Pageable pageable,
            StatisticsFilterDe filterDe
    ) {
        Page<OrganizerStatisticsView> organizerStatisticsViewList;

        Sort sort = pageable.getSort();

        if(sort.isSorted()) {
            Sort.Order sortOrder = sort.stream().findFirst().orElseThrow();
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    JpaSort.unsafe(
                            sortOrder.getDirection(),
                            String.format("(%s)", sortOrder.getProperty())
                    )
            );
        }

        switch (filterDe) {
            case Internal:
                organizerStatisticsViewList = organizerStatisticsRepository
                        .findInternalOrganizerStatistics(start, end, sessionTenant.getTenantId(), pageable);
                break;
            case External:
                organizerStatisticsViewList = organizerStatisticsRepository
                        .findExternalOrganizerStatistics(start, end, sessionTenant.getTenantId(), pageable);
                break;
            default:
                throw new RuntimeException("invalid filter: " + filterDe);

        }

        return organizerStatisticsViewList.map(dao -> modelMapper.map(dao, OrganizerStatisticsDm.class));
    }

    @Override
    public Page<OrganizerStatisticsAggregateDm> getAggregateOrganizersStatistics(
            Instant start,
            Instant end,
            Pageable pageable
    ) {
        Page<OrganizerStatisticsAggregateView> organizerStatisticsViewList;

        Sort sort = pageable.getSort();

        if(sort.isSorted()) {
            Sort.Order sortOrder = sort.stream().findFirst().orElseThrow();
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    JpaSort.unsafe(
                            sortOrder.getDirection(),
                            String.format("(%s)", sortOrder.getProperty())
                    )
            );
        }

        organizerStatisticsViewList = organizerStatisticsRepository
                .findAllOrganizerStatistics(start, end, sessionTenant.getTenantId(), pageable);

        return organizerStatisticsViewList.map(dao -> modelMapper.map(dao, OrganizerStatisticsAggregateDm.class));
    }
}
