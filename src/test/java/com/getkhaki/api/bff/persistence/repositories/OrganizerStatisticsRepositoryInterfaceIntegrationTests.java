package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import com.getkhaki.api.bff.persistence.models.views.OrganizerStatisticsView;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import javax.inject.Inject;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrganizerStatisticsRepositoryInterfaceIntegrationTests extends BaseJpaIntegrationTest {
    @Inject
    private OrganizerStatisticsRepositoryInterface underTest;

    @Test
    public void testFindExternal() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-30T00:00:00.000Z");
//        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "(totalMeetings)"));
        Pageable pageable = PageRequest.of(0, 2, JpaSort.unsafe(Sort.Direction.DESC, "(totalMeetings)"));
        Page<OrganizerStatisticsView> stats = underTest.findExternalOrganizerStatistics(
                start,
                end,
                s56OrgUuid,
                pageable
        );

        var array = stats.stream().toArray(OrganizerStatisticsView[]::new);
        var bobEmail = array[0].getOrganizerEmail();
        var bettyEmail = array[1].getOrganizerEmail();

        OrganizerStatisticsView bettyStats = stats
                .stream()
                .filter(stat -> {
                    var email = stat.getOrganizerEmail();
                    return stat.getOrganizerEmail().equals("betty@s56.net");
                })
                .findFirst()
                .orElseThrow();
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
        assertThat(bobStats.getTotalMeetings()).isEqualTo(3);
        assertThat(bobStats.getTotalSeconds()).isEqualTo(25200);
        assertThat(bobStats.getOrganizerFirstName()).isEqualTo("Bob");
        assertThat(bobStats.getOrganizerLastName()).isEqualTo("Jones");
    }

    @Test
    public void testFindInternal() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-30T00:00:00.000Z");
        Pageable pageable = PageRequest.of(0, 2);
        Page<OrganizerStatisticsView> stats = underTest.findInternalOrganizerStatistics(
                start,
                end,
                s56OrgUuid,
                pageable
        );

        assertThat(stats.getTotalElements()).isEqualTo(2);
        OrganizerStatisticsView bettyStats = stats
                .stream()
                .filter(stat -> {
                    var email = stat.getOrganizerEmail();
                    return stat.getOrganizerEmail().equals(email);
                })
                .findFirst()
                .orElseThrow();
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
        assertThat(bobStats.getTotalSeconds()).isEqualTo(7200);
        assertThat(bobStats.getOrganizerFirstName()).isEqualTo("Bob");
        assertThat(bobStats.getOrganizerLastName()).isEqualTo("Jones");
    }

}
