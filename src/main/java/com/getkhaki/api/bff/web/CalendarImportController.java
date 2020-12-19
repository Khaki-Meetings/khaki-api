package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.CalendarEventService;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/calendar-imports")
@CrossOrigin(origins = "*")
public class CalendarImportController {
    private final CalendarEventService calendarEventService;
    @Value("${com.getkhaki.api.bff.calendar-import-history-minutes:8760}")
    private int importMinutes;

    @Inject
    public CalendarImportController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @PostMapping("/{adminEmail}")
    public void importAsync(@PathVariable String adminEmail) {
        val timeAgo = Instant.now();
        this.calendarEventService.importAsync(adminEmail, timeAgo);
    }
}
