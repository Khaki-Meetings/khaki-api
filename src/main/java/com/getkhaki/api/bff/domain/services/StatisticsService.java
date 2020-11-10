package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.IntervalEnumDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class StatisticsService implements OrganizersStatisticsPersistenceInterface, TimeBlockSummaryPersistenceInterface, DepartmentStatisticsPersistenceInterface {

    private DepartmentStatisticsPersistenceService departmentStatisticsPersistenceService;
    private OrganizersStatisticsPersistenceService organizersStatisticsPersistenceService;
    private  TimeBlockSummaryPersistenceService timeBlockSummaryPersistenceService;
    private final ModelMapper modelMapper;

    public StatisticsService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.departmentStatisticsPersistenceService=new DepartmentStatisticsPersistenceService(this.modelMapper);
        this.organizersStatisticsPersistenceService=new OrganizersStatisticsPersistenceService(this.modelMapper);
        this.timeBlockSummaryPersistenceService=new TimeBlockSummaryPersistenceService(this.modelMapper);
    }


    @Override
    public DepartmentStatisticsDm getPerDepartmentStatistics(ZonedDateTime start, ZonedDateTime end) {
        return this.departmentStatisticsPersistenceService.getPerDepartmentStatistics(start,end);

    }

    @Override
    public OrganizersStatisticsDm getOrganizerStatistics(String email) {
        return this.organizersStatisticsPersistenceService.getOrganizerStatistics(email);
    }

    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(ZonedDateTime start, ZonedDateTime end) {
        return this.timeBlockSummaryPersistenceService.getTimeBlockSummary(start,end);
    }

    @Override
    public List<TimeBlockSummaryDm> getTrailingStatistics(ZonedDateTime start, ZonedDateTime end, IntervalEnumDto interval) {
        return this.timeBlockSummaryPersistenceService.getTrailingStatistics(start,end,interval);
    }


}
