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

        UUID bobUuid = UUID.fromString("580cf117-aee1-433e-90ed-51c23a9b6e47");

        var view = underTest.findIndividualExternalTimeBlockSummaryInRange(bobUuid, start, end, s56OrgUuid);

        view.forEach(
                item -> {
                    var a = item.getPersonId();
                    var b = item.getTotalSeconds();
                    var c = item.getMeetingCount();
                    var d = item.getFirstName();
                }
        );

//        var uuid = view.orElseThrow().getPersonId();
//        var seconds = view.orElseThrow().getTotalSeconds();

//        Long count = view.getMeetingCount();

//        assertThat(seconds).isEqualTo(39600);
//        assertThat(count).isEqualTo(2);
    }

    @Test
    public void testIndividualInternalFindBlockSummaryInRange() {
//        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
//        Instant end = Instant.parse("2020-11-10T00:00:00.000Z");
//
//        UUID bobUuid = UUID.fromString("0466ddc-6e6f-5451-c9d0-88d0f038c636a");
//
//        TimeBlockSummaryView view = underTest.findIndividualInternalTimeBlockSummaryInRange(bobUuid, start, end, s56OrgUuid);
//
//        Long seconds = view.getTotalSeconds();
//        Long count = view.getMeetingCount();
//
//        assertThat(seconds).isEqualTo(39600);
//        assertThat(count).isEqualTo(2);
    }
}
