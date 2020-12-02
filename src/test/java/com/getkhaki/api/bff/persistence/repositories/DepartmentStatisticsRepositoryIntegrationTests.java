package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.BaseJpaIntegrationTest;
import com.getkhaki.api.bff.persistence.models.views.DepartmentStatisticsView;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class DepartmentStatisticsRepositoryIntegrationTests extends BaseJpaIntegrationTest {
    @Inject
    private DepartmentStatisticsRepositoryInterface underTest;

    @Test
    public void findStatsInRage() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-08T00:00:00.000Z");
        List<DepartmentStatisticsView> ret = underTest.findAllDepartmentStatisticsInRange(start, end, s56OrgUuid);

        DepartmentStatisticsView hrStats = ret.stream().filter(item -> item.getDepartmentName().equals("HR"))
                .findFirst()
                .orElseThrow();
        assertThat(hrStats.getDepartmentId()).isNotNull();
        assertThat(hrStats.getTotalHours()).isEqualTo(5);

        DepartmentStatisticsView itStats = ret.stream().filter(item -> item.getDepartmentName().equals("IT"))
                .findFirst()
                .orElseThrow();
        assertThat(itStats.getDepartmentId()).isNotNull();
        assertThat(itStats.getTotalHours()).isEqualTo(8);
    }

    @Test
    public void findNothingWithWrongOrgId() {
        Instant start = Instant.parse("2020-11-01T00:00:00.000Z");
        Instant end = Instant.parse("2020-11-08T00:00:00.000Z");
        List<DepartmentStatisticsView> ret = underTest.findAllDepartmentStatisticsInRange(
                start,
                end,
                UUID.randomUUID()
        );

        assertThat(ret.size()).isEqualTo(0);
    }
}
