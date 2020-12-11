package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.CalendarEventService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
@RequestMapping("/calendar-imports")
@CrossOrigin(origins = "*")
public class CalendarImportController {
    private final CalendarEventService calendarEventService;

    @Inject
    public CalendarImportController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @PostMapping("/{adminEmail}")
    public void importAsync(@PathVariable String adminEmail) {
        this.calendarEventService.importAsync(adminEmail);
    }
}
