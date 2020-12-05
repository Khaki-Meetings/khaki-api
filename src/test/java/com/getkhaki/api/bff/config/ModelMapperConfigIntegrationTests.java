package com.getkhaki.api.bff.config;

import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelMapperConfigIntegrationTests {

    ModelMapper underTest;

    @BeforeEach
    public void setup() {
        underTest = new ModelMapperConfig().modelMapper();
    }

    @Test
    public void organizerStatisticsListToOrganizersStatisticsResponseDto() {
        OrganizerStatisticsDm mockDm1 = OrganizerStatisticsDm.builder()
                .organizerEmail("bob@bob.com")
                .totalCost(12.0)
                .totalMeetings(123)
                .totalHours(194)
                .build();

        OrganizerStatisticsResponseDto dto = underTest.map(mockDm1, OrganizerStatisticsResponseDto.class);

        assertThat(dto.getTotalCost()).isEqualTo(mockDm1.getTotalCost());
        assertThat(dto.getTotalMeetings()).isEqualTo(mockDm1.getTotalMeetings());
        assertThat(dto.getTotalHours()).isEqualTo(mockDm1.getTotalHours());
        assertThat(dto.getOrganizerEmail()).isEqualTo(mockDm1.getOrganizerEmail());
        assertThat(dto.getOrganizerEmail()).isEqualTo(mockDm1.getOrganizerEmail());
    }
}
