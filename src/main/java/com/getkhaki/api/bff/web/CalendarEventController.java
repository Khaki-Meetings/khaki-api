package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.web.models.*;
import com.getkhaki.api.bff.domain.services.CalendarEventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RequestMapping("/calendar-events")
@RestController
@CrossOrigin(origins = "*")
public class CalendarEventController {
    private final CalendarEventService calendarEventService;
    private final ModelMapper modelMapper;

    @Autowired
    public CalendarEventController(CalendarEventService calendarEventService, ModelMapper modelMapper) {
        this.calendarEventService = calendarEventService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public CalendarEventDto createEvent(@RequestBody CalendarEventDto calendarEventDto) {
        CalendarEventDm calendarEventDm = modelMapper.map(calendarEventDto, CalendarEventDm.class);
        CalendarEventDm calendarEventDmReturn = calendarEventService.createEvent(calendarEventDm);
        return modelMapper.map(calendarEventDmReturn, CalendarEventDto.class);
    }

    @GetMapping("/{start}/{end}")
    public Page<CalendarEventsWithAttendeesResponseDto> getCalendarEventsAttendees(
        @PathVariable Instant start,
        @PathVariable Instant end,
        @RequestParam(required = false) String organizer,
        Pageable pageable) {
        Page<CalendarEventsWithAttendeesResponseDto> calendarEventsWithAttendeesDmList = calendarEventService
                .getCalendarEventsAttendees(start, end, organizer, pageable);
        return calendarEventsWithAttendeesDmList.map(dm -> modelMapper.map(dm, CalendarEventsWithAttendeesResponseDto.class));
    }
}
