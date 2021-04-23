package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.models.TimeBlockSummaryDm;
import com.getkhaki.api.bff.domain.services.KhakiModelMapper;
import com.getkhaki.api.bff.persistence.models.TimeBlockSummaryDao;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoToDmTimeBlockSummaryIntegrationTests extends BaseModelMapperIntegrationTests {

    private KhakiModelMapper underTest;

    @BeforeEach
    public void setup() {
        underTest = new KhakiModelMapper();
    }

    @Test
    public void daoToDm() {
        UUID organizationId = UUID.randomUUID();
        long meetingLengthSeconds = 1000;

        val timeBlockSummaryDao = new TimeBlockSummaryDao()
                .setOrganizationId(organizationId)
                .setFilter(StatisticsFilterDe.Internal.toString())
                .setMeetingLengthSeconds(meetingLengthSeconds);

        val timeBlockSummaryDm =
                underTest.mapTimeBlockSummaryDaoToDm(timeBlockSummaryDao);

        assertThat(timeBlockSummaryDm.getOrganizationId()).isEqualTo(organizationId);
        assertThat(timeBlockSummaryDm.getFilterDe()).isEqualTo(StatisticsFilterDe.Internal);
        assertThat(timeBlockSummaryDm.getMeetingLengthSeconds()).isEqualTo(meetingLengthSeconds);

    }
}
