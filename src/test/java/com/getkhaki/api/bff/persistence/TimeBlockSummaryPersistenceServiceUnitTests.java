package com.getkhaki.api.bff.persistence;


import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)

public class TimeBlockSummaryPersistenceServiceUnitTests {

    private TimeBlockSummaryPersistenceService underTest;

    @Mock
    private TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        underTest = new TimeBlockSummaryPersistenceService(
                modelMapper,
                timeBlockSummaryRepositoryInterface,
                new SessionTenant().setTenantId(UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4"))
        );
    }

    @Test
    public void findTimeBlockSummaryInRange() {
        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");
        UUID id = UUID.randomUUID();
        TimeBlockSummaryDm timeBlockSummaryDm = new TimeBlockSummaryDm(1L, 1);
    }

    @Test
    public void getTrailingStatistics() {

    }
}
