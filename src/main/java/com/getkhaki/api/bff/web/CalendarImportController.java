package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.CalendarEventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/calendar-imports")
@RestController
public class CalendarImportController {
    private final CalendarEventService calendarEventService;

    @Autowired
    public CalendarImportController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @PostMapping("/{adminEmail}")
    public void importAsync(@PathVariable String adminEmail) {
        this.calendarEventService.importAsync(adminEmail);
    }
}
