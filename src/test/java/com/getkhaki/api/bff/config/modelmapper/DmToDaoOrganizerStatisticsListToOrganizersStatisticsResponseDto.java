package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DmToDaoOrganizerStatisticsListToOrganizersStatisticsResponseDto extends BaseModelMapperIntegrationTests {

    @Test
    public void organizerStatisticsListToOrganizersStatisticsResponseDto() {
        OrganizerStatisticsDm mockDm1 = OrganizerStatisticsDm.builder()
                .organizerEmail("bob@bob.com")
                .totalCost(12.0)
                .totalMeetings(123)
                .totalSeconds(194)
                .build();

        OrganizerStatisticsResponseDto dto = underTest.map(mockDm1, OrganizerStatisticsResponseDto.class);

        assertThat(dto.getTotalCost()).isEqualTo(mockDm1.getTotalCost());
        assertThat(dto.getTotalMeetings()).isEqualTo(mockDm1.getTotalMeetings());
        assertThat(dto.getTotalSeconds()).isEqualTo(mockDm1.getTotalSeconds());
        assertThat(dto.getOrganizerEmail()).isEqualTo(mockDm1.getOrganizerEmail());
    }
}
