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

        assertThat(seconds).isEqualTo(57600);
        assertThat(count).isEqualTo(4);
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

        var view = underTest.findIndividualExternalTimeBlockSummaryInRange(bobUuid, start, end, s56OrgUuid);

                    var a = view.getPersonId();
                    var e = view.getFirstName();
                    var b = view.getTotalSeconds();
                    var c = view.getMeetingCount();
                    var d = view.getFirstName();

        var foo = 1;
    }

    @Test
    public void testIndividualInternalFindBlockSummaryInRange() {
    }
}
