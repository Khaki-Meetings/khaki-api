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

public class DmToDaoTimeBlockSummaryIntegrationTests  extends BaseModelMapperIntegrationTests {

    private KhakiModelMapper underTest;

    @BeforeEach
    public void setup() {
        underTest = new KhakiModelMapper();
    }

    @Test
    public void daoToDm() {
        UUID organizationId = UUID.randomUUID();
        long meetingLengthSeconds = 1000;

        val timeBlockSummaryDm = new TimeBlockSummaryDm()
                .setOrganizationId(organizationId)
                .setFilterDe(StatisticsFilterDe.Internal)
                .setMeetingLengthSeconds(meetingLengthSeconds);

        val timeBlockSummaryDao =
                underTest.mapTimeBlockSummaryDmToDao(timeBlockSummaryDm);

        assertThat(timeBlockSummaryDao.getOrganizationId()).isEqualTo(organizationId);
        assertThat(timeBlockSummaryDao.getFilter()).isEqualTo(StatisticsFilterDe.Internal.toString());
        assertThat(timeBlockSummaryDao.getMeetingLengthSeconds()).isEqualTo(meetingLengthSeconds);

    }
}
