package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.persistence.models.views.TimeBlockSummaryView;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class TimeBlockSummaryPersistenceServiceUnitTests {
    private TimeBlockSummaryPersistenceService underTest;
    @Mock
    private TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private SessionTenant sessionTenant;

    @BeforeEach
    public void setup() {
        underTest = new TimeBlockSummaryPersistenceService(
                modelMapper, timeBlockSummaryRepositoryInterface, sessionTenant,
                goalRepositoryInterface);
    }

    @Test
    public void getTimeBlockSummary_withTenant() {
    }

    @Test
    public void getTimeBlockSummary_withoutTenant_Internal() {
    }

    @Test
    public void getTimeBlockSummary_withoutTenant_External() {
    }

    @Test
    public void getIndividualTimeBlockSummary() {
        var employeeId = UUID.randomUUID();
        var start = Instant.now();
        var end = Instant.now();
        var filterDe = StatisticsFilterDe.Internal;
        var tenantId = UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4");
        var timeBlockSummaryView = mock(TimeBlockSummaryView.class);
        var timeBlockSummaryDm = mock(TimeBlockSummaryDm.class);

        when(sessionTenant.getTenantId())
                .thenReturn(tenantId);

        when(timeBlockSummaryRepositoryInterface.findIndividualInternalTimeBlockSummaryInRange(
                employeeId, start, end, tenantId
        )).thenReturn(timeBlockSummaryView);

        when(timeBlockSummaryRepositoryInterface.findIndividualExternalTimeBlockSummaryInRange(
                employeeId, start, end, tenantId
        )).thenReturn(timeBlockSummaryView);

        when(modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class))
                .thenReturn(timeBlockSummaryDm);

        TimeBlockSummaryDm internalResult = underTest.getIndividualTimeBlockSummary(
                employeeId, start, end, StatisticsFilterDe.Internal
        );

        assertThat(internalResult).isEqualTo(timeBlockSummaryDm);

        TimeBlockSummaryDm externalResult = underTest.getIndividualTimeBlockSummary(
                employeeId, start, end, StatisticsFilterDe.External
        );

        assertThat(externalResult).isEqualTo(timeBlockSummaryDm);
    }

}
