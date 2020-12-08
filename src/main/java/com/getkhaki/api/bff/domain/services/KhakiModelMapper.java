package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.google.api.services.calendar.model.Event;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class KhakiModelMapper extends ModelMapper {
    public CalendarEventDm mapEventToCalendarEventDm(Event source) {
        return CalendarEventDm.builder()
                .googleCalendarId(source.getId())
                .summary(source.getSummary())
                .created(Instant.ofEpochMilli(source.getCreated().getValue()))
                .start(Instant.ofEpochMilli(source.getStart().getDateTime().getValue()))
                .end(Instant.ofEpochMilli(source.getEnd().getDateTime().getValue()))
                .build();
    }
}
