package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.web.models.CalendarEventDto;
import com.getkhaki.api.bff.domain.services.CalendarEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/calendar-events")
@RestController
public class CalendarEventController {
    private final CalendarEventService calendarEventService;

    @Autowired
    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @GetMapping
    public String getHello() {
        return "hello";
    }

    @PostMapping
    public CalendarEventDto createEvent() {
        return CalendarEventDto.builder().build();
    }
}
