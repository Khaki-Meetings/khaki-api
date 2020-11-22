package com.getkhaki.api.bff.config;

import com.getkhaki.api.bff.domain.models.OrganizerDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.web.models.OrganizerStatisticsResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.UUID;

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
                .organizer(
                        OrganizerDm.builder()
                        .email("bob@bob.com")
                        .name("Bob")
                        .build()
                )
                .totalCost(12)
                .totalMeetings(123)
                .totalHours(194)
                .build();

        OrganizerStatisticsResponseDto dto = underTest.map(mockDm1, OrganizerStatisticsResponseDto.class);

        assertThat(dto.getTotalCost()).isEqualTo(mockDm1.getTotalCost());
        assertThat(dto.getTotalMeetings()).isEqualTo(mockDm1.getTotalMeetings());
        assertThat(dto.getTotalHours()).isEqualTo(mockDm1.getTotalHours());
        assertThat(dto.getOrganizer().getEmail()).isEqualTo(mockDm1.getOrganizer().getEmail());
        assertThat(dto.getOrganizer().getName()).isEqualTo(mockDm1.getOrganizer().getName());
    }
}
