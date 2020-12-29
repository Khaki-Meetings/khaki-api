package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.inject.Inject;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrganizerStatisticsRepositoryInterfaceIntegrationTests extends BaseJpaIntegrationTest {
    @Inject
    private OrganizerStatisticsRepositoryInterface underTest;

    @Test
    public void queryTest() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-10T00:00:00.000Z");
        Pageable pageable = PageRequest.of(0,2);
        Page<OrganizerStatisticsView> stats = underTest.findExternalOrganizerStatistics(
                start,
                end,
                UUID.fromString("d713ace2-0d30-43be-b4ba-db973967d6d4"),
                pageable
        );

        var totalEl = stats.getTotalElements();
        var totalpage = stats.getTotalPages();
//        assertThat(stats.getTotalElements()).isEqualTo(2);
        stats.forEach(
                stat -> {
                    var email = stat.getOrganizerEmail();
                    var bla = stat.getOrganizerEmail();
                }
        );
//        OrganizerStatisticsView bettyStats = stats
//                .stream()
//                .filter(stat -> {
//                    var email = stat.getOrganizerEmail();
//                    return stat.getOrganizerEmail().equals("betty@s56.net");
//                })
//                .findFirst()
//                .orElseThrow();
//        var cost = bettyStats.getTotalCost();
//        var totalSeconds = bettyStats.getTotalSeconds();
//        var firstName = bettyStats.getOrganizerFirstName();
//        assertThat(bettyStats.getTotalCost()).isEqualTo(1282.5);
//        assertThat(bettyStats.getTotalSeconds()).isEqualTo(32400);
//        assertThat(bettyStats.getTotalMeetings()).isEqualTo(1);
//        assertThat(bettyStats.getOrganizerFirstName()).isEqualTo("Betty");
//        assertThat(bettyStats.getOrganizerLastName()).isEqualTo("Smith");

//
//        OrganizerStatisticsView bobStats = stats
//                .stream()
//                .filter(stat -> stat.getOrganizerEmail().equals("bob@s56.net"))
//                .findFirst()
//                .orElseThrow();
//        assertThat(bobStats.getOrganizerEmail()).isEqualTo("bob@s56.net");
//        assertThat(bobStats.getTotalMeetings()).isEqualTo(1);
//        assertThat(bobStats.getTotalSeconds()).isEqualTo(14400);
//        assertThat(bobStats.getTotalCost()).isEqualTo(380.0);
//        assertThat(bobStats.getOrganizerFirstName()).isEqualTo("Bob");
//        assertThat(bobStats.getOrganizerLastName()).isEqualTo("Jones");
    }
}
