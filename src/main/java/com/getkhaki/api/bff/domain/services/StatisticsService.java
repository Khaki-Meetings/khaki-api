package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizersStatisticsPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.TimeBlockSummaryPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService implements OrganizersStatisticsPersistenceInterface, TimeBlockSummaryPersistenceInterface, DepartmentStatisticsPersistenceInterface {
    @Override
    public DepartmentStatisticsDm getPerDepartmentStatistics(ZonedDateTime start, ZonedDateTime end) {
        return new  DepartmentStatisticsDm();

    }

    @Override
    public OrganizersStatisticsDm getOrganizerStatistics(String email) {
        return new OrganizersStatisticsDm();
    }

    @Override
    public TimeBlockSummaryDm getTimeBlockSummary(ZonedDateTime start, ZonedDateTime end) {
        return new TimeBlockSummaryDm();
    }

    @Override
    public List<TimeBlockSummaryDm> getTrailingStatistics(ZonedDateTime start, ZonedDateTime end, IntervalEnumDto interval) {
        List<TimeBlockSummaryDm> list=new ArrayList<>();
        return list;
    }


/*
    public OrganizersStatisticsResponseDto getOrganizersStatistics(ZonedDateTime start, ZonedDateTime end) {

        return modelMapper.map(new  OrganizersStatisticsResponseDto(), OrganizersStatisticsResponseDto.class);
    }


    @GetMapping("/summary")
    public TimeBlockSummaryResponseDto getTimeBlockSummary(ZonedDateTime start, ZonedDateTime end) {

        return modelMapper.map(new  TimeBlockSummaryResponseDto(), TimeBlockSummaryResponseDto.class);
    }

    @GetMapping("/department")
    public List<DepartmentStatisticsResponseDto> getPerDepartmentStatistics(ZonedDateTime start, ZonedDateTime end) {

        List<DepartmentStatisticsResponseDto> list=new ArrayList<>();
        return modelMapper.map(list, List.class);
    }

    @GetMapping("/trailing")
    public TrailingStatisticsResponseDto getTrailingStatistics(ZonedDateTime start, ZonedDateTime end) {

        return modelMapper.map(new  TrailingStatisticsResponseDto(), TrailingStatisticsResponseDto.class);
    }*/
}
