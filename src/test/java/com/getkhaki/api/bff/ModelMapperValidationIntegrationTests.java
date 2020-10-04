package com.getkhaki.api.bff;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelMapperValidationIntegrationTests extends BaseModelMapperIntegrationTests {
    @Test
    public void validate() {
        modelMapper.validate();
    }


}
