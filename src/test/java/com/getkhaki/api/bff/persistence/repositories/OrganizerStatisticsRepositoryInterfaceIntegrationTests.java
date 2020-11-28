package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import liquibase.exception.LiquibaseException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrganizerStatisticsRepositoryInterfaceIntegrationTests extends BaseJpaIntegrationTest {
    @Inject
    private OrganizerStatisticsRepositoryInterface underTest;

    @Inject
    private CalendarEventRepositoryInterface calendarEventRepository;

    @Test
    public void queryTest() throws LiquibaseException {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-08T00:00:00.000Z");
        List<OrganizerStatisticsView> stats = underTest.findAllOrganizerStatistics(
                start,
                end,
                UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4")
        );

        assertThat(stats.size()).isEqualTo(2);
        OrganizerStatisticsView bettyStats = stats
                .stream()
                .filter(stat -> stat.getOrganizerEmail().equals("betty@s56.net"))
                .findFirst()
                .orElseThrow();
        assertThat(bettyStats.getTotalCost()).isEqualTo(1282.5);
        assertThat(bettyStats.getTotalHours()).isEqualTo(9);
        assertThat(bettyStats.getTotalMeetingCount()).isEqualTo(1);

        OrganizerStatisticsView bobStats = stats
                .stream()
                .filter(stat -> stat.getOrganizerEmail().equals("bob@s56.net"))
                .findFirst()
                .orElseThrow();
        assertThat(bobStats.getOrganizerEmail()).isEqualTo("bob@s56.net");
        assertThat(bobStats.getTotalMeetingCount()).isEqualTo(1);
        assertThat(bobStats.getTotalHours()).isEqualTo(4);
        assertThat(bobStats.getTotalCost()).isEqualTo(380.0);
    }
}
