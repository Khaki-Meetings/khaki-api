package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.IntervalEnumDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import org.apache.commons.lang3.NotImplementedException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TimeBlockSummaryPersistenceService implements TimeBlockSummaryPersistenceInterface {
    private final ModelMapper modelMapper;

    public TimeBlockSummaryPersistenceService(ModelMapper modelMapper, TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface) {
        this.modelMapper = modelMapper;
        this.timeBlockSummaryRepositoryInterface = timeBlockSummaryRepositoryInterface;
    }

    private TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface;

    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end) {
        return modelMapper.map(
                timeBlockSummaryRepositoryInterface.findTimeBlockSummaryInRange(
                        start,
                        end,
                        UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4")
                ),
                TimeBlockSummaryDm.class
        );
    }
}
