package com.getkhaki.api.bff;

import com.getkhaki.api.bff.config.ModelMapperConfig;
import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelMapperValidationIntegrationTests {
    ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        ModelMapperConfig modelMapperConfig = new ModelMapperConfig();
        modelMapper = modelMapperConfig.modelMapper();
    }

    @Test
    public void validate() {
        modelMapper.validate();
    }

    @Test
    public void dtoToDm() {
        CalendarEventDto calendarEventDto = new CalendarEventDto()
                .setId(UUID.randomUUID())
                .setSummary("cignus")
                .setCreated(LocalDateTime.now());

        CalendarEventDm calendarEventDm = modelMapper.map(calendarEventDto, CalendarEventDm.class);

        assertThat(calendarEventDm).isNotNull();
        assertThat(calendarEventDm.getId()).isEqualTo(calendarEventDto.getId());
        assertThat(calendarEventDm.getSummary()).isEqualTo(calendarEventDto.getSummary());
        assertThat(calendarEventDm.getCreated()).isEqualTo(calendarEventDto.getCreated());

    }
}
