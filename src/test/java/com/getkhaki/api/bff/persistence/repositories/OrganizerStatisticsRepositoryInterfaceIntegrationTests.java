package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
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

    @Test
    public void queryTest() {
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
        assertThat(bettyStats.getTotalSeconds()).isEqualTo(32400);
        assertThat(bettyStats.getTotalMeetings()).isEqualTo(1);
        assertThat(bettyStats.getOrganizerFirstName()).isEqualTo("Betty");
        assertThat(bettyStats.getOrganizerLastName()).isEqualTo("Smith");


        OrganizerStatisticsView bobStats = stats
                .stream()
                .filter(stat -> stat.getOrganizerEmail().equals("bob@s56.net"))
                .findFirst()
                .orElseThrow();
        assertThat(bobStats.getOrganizerEmail()).isEqualTo("bob@s56.net");
        assertThat(bobStats.getTotalMeetings()).isEqualTo(1);
        assertThat(bobStats.getTotalSeconds()).isEqualTo(14400);
        assertThat(bobStats.getTotalCost()).isEqualTo(380.0);
        assertThat(bobStats.getOrganizerFirstName()).isEqualTo("Bob");
        assertThat(bobStats.getOrganizerLastName()).isEqualTo("Jones");
    }
}
