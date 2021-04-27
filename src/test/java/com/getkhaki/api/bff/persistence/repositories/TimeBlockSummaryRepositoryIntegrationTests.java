package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import com.getkhaki.api.bff.persistence.models.views.TimeBlockSummaryView;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TimeBlockSummaryRepositoryIntegrationTests extends BaseJpaIntegrationTest {
    @Inject
    private TimeBlockSummaryRepositoryInterface underTest;

    @Test
    public void testExternalFindBlockSummaryInRange() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-10T00:00:00.000Z");

        TimeBlockSummaryView view = underTest.findExternalTimeBlockSummaryInRange(start, end, s56OrgUuid);

        Long seconds = view.getTotalSeconds();
        Long count = view.getMeetingCount();

        assertThat(seconds).isEqualTo(18000);
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testInternalFindBlockSummaryInRange() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-10T00:00:00.000Z");

        TimeBlockSummaryView view = underTest.findInternalTimeBlockSummaryInRange(start, end, s56OrgUuid);

        Long seconds = view.getTotalSeconds();
        Long count = view.getMeetingCount();

        assertThat(seconds).isEqualTo(39600);
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testIndividualExternalFindBlockSummaryInRange() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-10T00:00:00.000Z");

        UUID bobUuid = UUID.fromString("f66d66d7-7b40-4ffe-a38a-aae70919a1ef");

        TimeBlockSummaryView view = underTest.findIndividualExternalTimeBlockSummaryInRange(bobUuid, start, end, s56OrgUuid);

        assertThat(view.getPersonId()).isEqualTo(UUID.fromString("580cf117-aee1-433e-90ed-51c23a9b6e47"));
        assertThat(view.getFirstName()).isEqualTo("bob");
        assertThat(view.getTotalSeconds()).isEqualTo(25200);
        assertThat(view.getMeetingCount()).isEqualTo(4);
    }

    @Test
    public void testIndividualInternalFindBlockSummaryInRange() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-10T00:00:00.000Z");

        UUID bobUuid = UUID.fromString("f66d66d7-7b40-4ffe-a38a-aae70919a1ef");

        TimeBlockSummaryView view = underTest.findIndividualInternalTimeBlockSummaryInRange(bobUuid, start, end, s56OrgUuid);

        assertThat(view.getPersonId()).isEqualTo(UUID.fromString("580cf117-aee1-433e-90ed-51c23a9b6e47"));
        assertThat(view.getFirstName()).isEqualTo("bob");
        assertThat(view.getTotalSeconds()).isEqualTo(14400);
        assertThat(view.getMeetingCount()).isEqualTo(2);
    }

    @Test
    public void testFindNumberOfWorkdaysBetweenDates() {

        Instant start = Instant.parse("2021-03-16T00:00:00.000Z");
        Instant end = Instant.parse("2021-03-23T00:00:00.000Z");
        Integer workdays = underTest.findNumberOfWorkdaysBetweenDates(start, end);
        assertThat(workdays).isEqualTo(5);

        start = Instant.parse("2021-03-09T00:00:00.000Z");
        end = Instant.parse("2021-03-23T00:00:00.000Z");
        workdays = underTest.findNumberOfWorkdaysBetweenDates(start, end);
        assertThat(workdays).isEqualTo(10);

        start = Instant.parse("2021-03-20T00:00:00.000Z");
        end = Instant.parse("2021-03-23T00:00:00.000Z");
        workdays = underTest.findNumberOfWorkdaysBetweenDates(start, end);
        assertThat(workdays).isEqualTo(1);

        start = Instant.parse("2021-03-19T00:00:00.000Z");
        end = Instant.parse("2021-03-23T00:00:00.000Z");
        workdays = underTest.findNumberOfWorkdaysBetweenDates(start, end);
        assertThat(workdays).isEqualTo(2);

    }
}
