package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.views.TimeBlockSummaryView;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Service
public class TimeBlockSummaryPersistenceService implements TimeBlockSummaryPersistenceInterface {
    private final ModelMapper modelMapper;
    private final TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface;
    private final SessionTenant sessionTenant;

    public TimeBlockSummaryPersistenceService(ModelMapper modelMapper, TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface, SessionTenant sessionTenant) {
        this.modelMapper = modelMapper;
        this.timeBlockSummaryRepositoryInterface = timeBlockSummaryRepositoryInterface;
        this.sessionTenant = sessionTenant;
    }

    @Async
    @Override
    public CompletableFuture<TimeBlockSummaryDm> getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe) {
        TimeBlockSummaryView timeBlockSummaryView;

        switch (filterDe) {
            case External:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findExternalTimeBlockSummaryInRange(
                        start,
                        end,
                        sessionTenant.getTenantId()
                );

                break;
            case Internal:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findInternalTimeBlockSummaryInRange(
                        start,
                        end,
                        sessionTenant.getTenantId()
                );

                break;
            default:
                throw new RuntimeException("invalid filter: " + filterDe);
        }

        return CompletableFuture.supplyAsync(() -> modelMapper.map(
                timeBlockSummaryView,
                TimeBlockSummaryDm.class
        ));
    }
}
