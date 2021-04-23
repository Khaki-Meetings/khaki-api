package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.IntervalDe;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticsServiceUnitTests {
    private StatisticsService underTest;
    private TimeBlockSummaryPersistenceInterface timeBlockSummaryPersistenceService;
    private TimeBlockGeneratorFactory timeBlockGeneratorFactory;
    private SessionTenant sessionTenant;
    private TimeBlockSummaryService timeBlockSummaryService;
    private OrganizationRepositoryInterface organizationRepository;

    @BeforeEach
    public void setup() {

        timeBlockGeneratorFactory = mock(TimeBlockGeneratorFactory.class);
        timeBlockSummaryService = mock(TimeBlockSummaryService.class);
        organizationRepository = mock(OrganizationRepositoryInterface.class);
        sessionTenant = new SessionTenant().setTenantId(UUID.randomUUID());
        underTest = new StatisticsService(
                timeBlockSummaryService, timeBlockGeneratorFactory,
                sessionTenant, organizationRepository);
        timeBlockSummaryPersistenceService = mock(TimeBlockSummaryPersistenceInterface.class);
    }

    @Test
    public void testTrailingStatisticsMonth() {
        Instant startTest = Instant.parse("2020-11-01T00:00:00.000Z");
        int count = 4;
        IntervalDe interval = IntervalDe.Month;

        when(timeBlockGeneratorFactory.get(any())).thenCallRealMethod();
        val firstStart = Instant.parse("2020-11-01T00:00:00.000Z");
        val firstEnd = Instant.parse("2020-11-30T23:59:59.000Z");
        val secondStart = Instant.parse("2020-10-01T00:00:00.000Z");
        val secondEnd = Instant.parse("2020-10-30T23:59:59.000Z");
        val filter = StatisticsFilterDe.Internal;
        when(
                timeBlockSummaryService.getTimeBlockSummary(
                        eq(firstStart),
                        eq(firstEnd),
                        eq(filter),
                        eq(sessionTenant.getTenantId())
                )
        )
                .thenReturn(new TimeBlockSummaryDm(UUID.randomUUID(), StatisticsFilterDe.Internal,
                        1L, 1, firstStart, firstEnd, 1, 1, 1, 0, 0L, 0));

        when(
                timeBlockSummaryService.getTimeBlockSummary(
                        eq(secondStart),
                        eq(secondEnd),
                        eq(filter),
                        eq(sessionTenant.getTenantId())
                )
        )
                .thenReturn(new TimeBlockSummaryDm(UUID.randomUUID(), StatisticsFilterDe.Internal,
                        1L, 1, secondStart, secondEnd, 1, 1, 1, 0, 0L, 0));

        val trailingStats = underTest.getTrailingStatistics(startTest, interval, count, StatisticsFilterDe.Internal);

        assertThat(trailingStats).isNotNull();
        assertThat(trailingStats).hasSize(count);

        val firstTimeBlockSummary = trailingStats.get(0);
        assertThat(firstTimeBlockSummary.getStart()).isEqualTo(firstStart);
        assertThat(firstTimeBlockSummary.getEnd()).isEqualTo(firstEnd);

        val secondTimeBlockSummary = trailingStats.get(1);
        assertThat(secondTimeBlockSummary.getStart()).isEqualTo(secondStart);
        assertThat(secondTimeBlockSummary.getEnd()).isEqualTo(secondEnd);
    }

}
