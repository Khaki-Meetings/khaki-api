package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.BaseIntegrationTest;
import com.getkhaki.api.bff.persistence.models.views.TimeBlockSummaryView;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TimeBlockSummaryRepositoryIntegrationTests extends BaseIntegrationTest {
    @Inject
    private TimeBlockSummaryRepositoryInterface underTest;

    @Test
    public void testFindBlockSummaryInRange() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-10T00:00:00.000Z");

        TimeBlockSummaryView view = underTest.findTimeBlockSummaryInRange(start, end, s56OrgUuid);

        Long hours = view.getTotalHours();
        Long count = view.getMeetingCount();

        assertThat(hours).isEqualTo(15);
        assertThat(count).isEqualTo(3);
    }
}
