package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.views.TimeBlockSummaryView;
import com.getkhaki.api.bff.persistence.repositories.TimeBlockSummaryRepositoryInterface;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TimeBlockSummaryPersistenceService implements TimeBlockSummaryPersistenceInterface {
    private final ModelMapper modelMapper;
    private final TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface;
    private final SessionTenant sessionTenant;

    public TimeBlockSummaryPersistenceService(
            ModelMapper modelMapper,
            TimeBlockSummaryRepositoryInterface timeBlockSummaryRepositoryInterface,
            SessionTenant sessionTenant
    ) {
        this.modelMapper = modelMapper;
        this.timeBlockSummaryRepositoryInterface = timeBlockSummaryRepositoryInterface;
        this.sessionTenant = sessionTenant;
    }

    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe) {
        return getTimeBlockSummary(start, end, filterDe, sessionTenant.getTenantId());

    }

    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(Instant start, Instant end, StatisticsFilterDe filterDe, UUID tenantId) {
        TimeBlockSummaryView timeBlockSummaryView;

        switch (filterDe) {
            case External:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findExternalTimeBlockSummaryInRange(
                        start, end, tenantId
                );

                break;
            case Internal:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findInternalTimeBlockSummaryInRange(
                        start, end, tenantId
                );

                break;
            default:
                throw new RuntimeException("invalid filter: " + filterDe);
        }

        val timeBlockSummaryDm = modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class);
        timeBlockSummaryDm.setEnd(end);
        timeBlockSummaryDm.setStart(start);

        return timeBlockSummaryDm;
    }

    @Override
    public TimeBlockSummaryDm getIndividualTimeBlockSummary(
            UUID employeeId, Instant start, Instant end, StatisticsFilterDe filterDe
    ) {
        TimeBlockSummaryView timeBlockSummaryView;

        switch (filterDe) {
            case External:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findIndividualExternalTimeBlockSummaryInRange(
                        employeeId, start, end, sessionTenant.getTenantId()
                );

                break;
            case Internal:
                timeBlockSummaryView = timeBlockSummaryRepositoryInterface.findIndividualInternalTimeBlockSummaryInRange(
                        employeeId, start, end, sessionTenant.getTenantId()
                );
                break;
            default:
                throw new RuntimeException("invalid filter: " + filterDe);
        }

        if (timeBlockSummaryView == null) {
            return new TimeBlockSummaryDm().setMeetingCount(0).setTotalSeconds(0L);
        }

        val timeBlockSummaryDm = modelMapper.map(timeBlockSummaryView, TimeBlockSummaryDm.class);
        timeBlockSummaryDm.setEnd(end);
        timeBlockSummaryDm.setStart(start);

        return timeBlockSummaryDm;
    }
}
