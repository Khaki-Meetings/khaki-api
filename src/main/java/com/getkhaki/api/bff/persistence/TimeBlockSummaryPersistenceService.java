package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

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


    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end) {
        return modelMapper.map(
                timeBlockSummaryRepositoryInterface.findTimeBlockSummaryInRange(
                        start,
                        end,
                        sessionTenant.getTenantId()
                ),
                TimeBlockSummaryDm.class
        );
    }
}
