package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDto;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class TimeBlockSummaryPersistenceService implements TimeBlockSummaryPersistenceInterface {
    private final ModelMapper modelMapper;

    public TimeBlockSummaryPersistenceService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface;

    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(ZonedDateTime start, ZonedDateTime end) {
        return modelMapper.map( timeBlockSummaryRepositoryInterface.findTimeBlockSummaryInRange(start,end),TimeBlockSummaryDm.class);
    }

    @Override
    public List<TimeBlockSummaryDm> getTrailingStatistics(ZonedDateTime start, ZonedDateTime end, IntervalEnumDto interval) {
        return null;
    }
}
