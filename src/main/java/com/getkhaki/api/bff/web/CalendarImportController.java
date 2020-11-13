package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.services.CalendarEventService;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/calendar-events")
@RestController
public class CalendarImportController {
    private final CalendarEventService calendarEventService;
    private final ModelMapper modelMapper;

    @Autowired
    public CalendarImportController(CalendarEventService calendarEventService, ModelMapper modelMapper) {
        this.calendarEventService = calendarEventService;
        this.modelMapper = modelMapper;
    }


    @PostMapping
    public void importAsync(@PathVariable String adminEmail) {

        this.calendarEventService.importAsync(adminEmail);
    }
}
