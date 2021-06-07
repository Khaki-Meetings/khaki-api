package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.*;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;


@CommonsLog
@Service
public class StatisticsService {
    private final TimeBlockSummaryService timeBlockSummaryService;
    private final TimeBlockGeneratorFactory timeBlockGeneratorFactory;
    private final SessionTenant sessionTenant;
    private final OrganizationRepositoryInterface organizationRepository;

    public StatisticsService(
            TimeBlockSummaryService timeBlockSummaryService,
            TimeBlockGeneratorFactory timeBlockGeneratorFactory,
            SessionTenant sessionTenant,
            OrganizationRepositoryInterface organizationRepository) {
        this.timeBlockSummaryService = timeBlockSummaryService;
        this.timeBlockGeneratorFactory = timeBlockGeneratorFactory;
        this.sessionTenant = sessionTenant;
        this.organizationRepository = organizationRepository;
    }

    public List<TimeBlockSummaryDm> getTrailingStatistics(
            Instant start,
            IntervalDe interval,
            int count,
            StatisticsFilterDe filterDe
    ) {
        TimeBlockGeneratorInterface timeBlockGenerator = timeBlockGeneratorFactory.get(interval);
        List<TimeBlockRangeDm> timeBlockRangeList = timeBlockGenerator.generate(start, count);

        UUID tenantId = sessionTenant.getTenantId();
        var futures = timeBlockRangeList
                .stream()
                .map(
                        range -> supplyAsync(() -> timeBlockSummaryService
                                .getTimeBlockSummary(
                                        range.getStart(),
                                        range.getEnd(),
                                        filterDe,
                                        tenantId
                                ))
                )
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public List<TimeBlockSummaryDm> getTrailingStatistics(
            Instant start,
            IntervalDe interval,
            int count,
            StatisticsFilterDe filterDe,
            UUID organizationId
    ) {
        TimeBlockGeneratorInterface timeBlockGenerator = timeBlockGeneratorFactory.get(interval);
        List<TimeBlockRangeDm> timeBlockRangeList = timeBlockGenerator.generate(start, count);

        var futures = timeBlockRangeList
                .stream()
                .map(
                        range -> supplyAsync(() -> timeBlockSummaryService
                                .getTimeBlockSummary(
                                        range.getStart(),
                                        range.getEnd(),
                                        filterDe,
                                        organizationId
                                ))
                )
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public List<TimeBlockSummaryDm> getDepartmentTrailingStatistics(
            Instant start,
            IntervalDe interval,
            int count,
            StatisticsFilterDe filterDe,
            String departmentName
    ) {
        TimeBlockGeneratorInterface timeBlockGenerator = timeBlockGeneratorFactory.get(interval);
        List<TimeBlockRangeDm> timeBlockRangeList = timeBlockGenerator.generate(start, count);

        UUID tenantId = sessionTenant.getTenantId();
        var futures = timeBlockRangeList
                .stream()
                .map(
                        range -> supplyAsync(() -> timeBlockSummaryService
                                .getDepartmentTimeBlockSummary(
                                        range.getStart(),
                                        range.getEnd(),
                                        filterDe,
                                        tenantId,
                                        departmentName
                                ))
                )
                .collect(Collectors.toList());

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    @Scheduled(cron = "0 1 0 * * *")
    public void buildTrailingStatistics() {
        log.info("Building Trailing Statistics");

        Calendar weekStartCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        weekStartCal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        weekStartCal.clear(Calendar.MINUTE);
        weekStartCal.clear(Calendar.SECOND);
        weekStartCal.clear(Calendar.MILLISECOND);
        weekStartCal.set(Calendar.DAY_OF_WEEK, weekStartCal.getFirstDayOfWeek());
        log.info("Start of this week:       " + weekStartCal.getTime().toInstant());

        Instant weeksStart = weekStartCal.getTime().toInstant();

        Calendar monthStartCal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        monthStartCal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        monthStartCal.clear(Calendar.MINUTE);
        monthStartCal.clear(Calendar.SECOND);
        monthStartCal.clear(Calendar.MILLISECOND);
        monthStartCal.set(Calendar.DAY_OF_MONTH, 1);
        log.info("Start of the month:       " + monthStartCal.getTime().toInstant());

        Instant monthsStart = monthStartCal.getTime().toInstant();

        List<OrganizationDao> organizationDaos = organizationRepository.findAll();

        for (OrganizationDao organizationDao : organizationDaos) {
            UUID organizationId = organizationDao.getId();
            log.info("Organization Info: " + organizationId);

            for (StatisticsFilterDe statisticsFilterDe : StatisticsFilterDe.values()) {
                List<TimeBlockSummaryDm> statistics =
                        getTrailingStatistics(weeksStart, IntervalDe.Week, 12,
                                statisticsFilterDe, organizationId);
                statistics.addAll(
                        getTrailingStatistics(monthsStart, IntervalDe.Month, 12,
                                statisticsFilterDe, organizationId));
            }
        }
    }
}
