package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrganizerStatisticsRepositoryInterfaceIntegrationTests {
    @Inject
    private OrganizerStatisticsRepositoryInterface underTest;

    @Inject
    private CalendarEventRepositoryInterface calendarEventRepository;

    @Inject
    SpringLiquibase liquibase;

    @Test
    public void queryTest() throws LiquibaseException {
        loadData();
        Instant start = Instant.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        Instant end = Instant.parse("2020-11-08T00:00:00.000000-07:00[America/Denver]");
        List<OrganizerStatisticsView> stats = underTest.findAllOrganizerStatistics(
                start,
                end,
                UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4")
        );

        stats.stream().forEach(
                stat -> {
                    String organizerEmail = stat.getOrganizerEmail();
                    Integer totalMeetingCount = stat.getTotalMeetingCount();
                    Integer totalHours = stat.getTotalHours();
                    Double totalCost = stat.getTotalCost();
                }
        );


    }

    private void loadData() throws LiquibaseException {
        liquibase.setChangeLog("classpath:fixtures/liquibase-test-data.yaml");
        liquibase.setShouldRun(true);
        liquibase.afterPropertiesSet();

//        List<CalendarEventDao> calendarEvent = calendarEventRepository.findAll();
//        CalendarEventParticipantDao calendarEventParticipant = new CalendarEventParticipantTestData().getData().get(0);
    }
}
