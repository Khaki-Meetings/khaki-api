package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import com.getkhaki.api.bff.persistence.repositories.OrganizerStatisticsRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrganizersStatisticsPersistenceServiceUnitTests {
    private OrganizersStatisticsPersistenceService underTest;

    @Mock
    private OrganizerStatisticsRepositoryInterface mockOrganizerStatisticsRepository;
    @Mock
    private ModelMapper mockMapper;
    @Mock
    private SessionTenant mockSessionTenant;

    @BeforeEach
    public void setup() {
        underTest = new OrganizersStatisticsPersistenceService(
                mockOrganizerStatisticsRepository,
                mockMapper,
                mockSessionTenant
        );
    }

    @Test
    public void getOrganizersStatistics_Internal() {
        var start = Instant.now();
        var end = Instant.now();
        var tenantId = UUID.randomUUID();
        var pageable = PageRequest.of(0, 2);
        var mockOrganizerStatisticsView = mock(OrganizerStatisticsView.class);
        var viewPage = new PageImpl<OrganizerStatisticsView>(List.of(mockOrganizerStatisticsView));
        var mockOrganizerStatisticsDm = mock(OrganizerStatisticsDm.class);
        var dmPage = new PageImpl<OrganizerStatisticsDm>(List.of(mockOrganizerStatisticsDm));

        when(this.mockSessionTenant.getTenantId())
                .thenReturn(tenantId);

        when(this.mockOrganizerStatisticsRepository.findInternalOrganizerStatistics(start, end, tenantId, pageable))
                .thenReturn(viewPage);

        when(this.mockMapper.map(mockOrganizerStatisticsView, OrganizerStatisticsDm.class))
                .thenReturn(mockOrganizerStatisticsDm);

        Page<OrganizerStatisticsDm> result = this.underTest.getOrganizersStatistics(
                start,
                end,
                pageable,
                StatisticsFilterDe.Internal
        );

        assertThat(result).isEqualTo(dmPage);
    }

    @Test
    public void getOrganizersStatistics_External() {
        var start = Instant.now();
        var end = Instant.now();
        var tenantId = UUID.randomUUID();
        var pageable = PageRequest.of(0, 2);
        var mockOrganizerStatisticsView = mock(OrganizerStatisticsView.class);
        var viewPage = new PageImpl<OrganizerStatisticsView>(List.of(mockOrganizerStatisticsView));
        var mockOrganizerStatisticsDm = mock(OrganizerStatisticsDm.class);
        var dmPage = new PageImpl<OrganizerStatisticsDm>(List.of(mockOrganizerStatisticsDm));

        when(this.mockSessionTenant.getTenantId())
                .thenReturn(tenantId);

        when(this.mockOrganizerStatisticsRepository.findExternalOrganizerStatistics(start, end, tenantId, pageable))
                .thenReturn(viewPage);

        when(this.mockMapper.map(mockOrganizerStatisticsView, OrganizerStatisticsDm.class))
                .thenReturn(mockOrganizerStatisticsDm);

        Page<OrganizerStatisticsDm> result = this.underTest.getOrganizersStatistics(
                start,
                end,
                pageable,
                StatisticsFilterDe.External
        );

        assertThat(result).isEqualTo(dmPage);
    }
}
