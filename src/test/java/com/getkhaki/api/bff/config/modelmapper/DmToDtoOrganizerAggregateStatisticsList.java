package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.OrganizerStatisticsAggregateDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsAggregateResponseDto;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DmToDtoOrganizerAggregateStatisticsList extends BaseModelMapperIntegrationTests {

    @Test
    public void organizerAggregateStatisticsListToOrganizersAggregateStatisticsResponseDto() {
        OrganizerStatisticsAggregateDm mockDm1 = OrganizerStatisticsAggregateDm.builder()
                .organizerEmail("bob@bob.com")
                .internalMeetingCount(3)
                .internalMeetingSeconds(3600)
                .externalMeetingCount(2)
                .externalMeetingSeconds(7200)
                .build();

        OrganizerStatisticsAggregateResponseDto dto = underTest.map(mockDm1, OrganizerStatisticsAggregateResponseDto.class);

        assertThat(dto.getInternalMeetingCount()).isEqualTo(mockDm1.getInternalMeetingCount());
        assertThat(dto.getInternalMeetingSeconds()).isEqualTo(mockDm1.getInternalMeetingSeconds());
        assertThat(dto.getExternalMeetingCount()).isEqualTo(mockDm1.getExternalMeetingCount());
        assertThat(dto.getExternalMeetingSeconds()).isEqualTo(mockDm1.getExternalMeetingSeconds());
        assertThat(dto.getOrganizerEmail()).isEqualTo(mockDm1.getOrganizerEmail());
    }
}
