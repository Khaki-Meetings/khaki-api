package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.CalendarEventDetailDm;
import com.getkhaki.api.bff.web.models.CalendarEventsWithAttendeesResponseDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DMtoDtoCalendarEventDetailToCalendarEventsWithAttendeesResponse extends BaseModelMapperIntegrationTests {

    @Test
    public void organizerStatisticsListToOrganizersStatisticsResponseDto() {
        CalendarEventDetailDm mockDm1 = CalendarEventDetailDm.builder()
                .organizerEmail("bob@bob.com")
                .totalSeconds(194)
                .build();

        CalendarEventsWithAttendeesResponseDto dto = underTest.map(mockDm1, CalendarEventsWithAttendeesResponseDto.class);

        assertThat(dto.getTotalSeconds()).isEqualTo(mockDm1.getTotalSeconds());
        assertThat(dto.getOrganizerEmail()).isEqualTo(mockDm1.getOrganizerEmail());
    }
}