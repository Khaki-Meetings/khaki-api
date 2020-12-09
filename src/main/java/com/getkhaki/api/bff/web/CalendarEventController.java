package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import com.getkhaki.api.bff.domain.services.CalendarEventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public String getHello() {
        return "hello";
    }

    @PostMapping
    public CalendarEventDto createEvent(@RequestBody CalendarEventDto calendarEventDto) {
        CalendarEventDm calendarEventDm = modelMapper.map(calendarEventDto, CalendarEventDm.class);
        CalendarEventDm calendarEventDmReturn = calendarEventService.createEvent(calendarEventDm);
        return modelMapper.map(calendarEventDmReturn, CalendarEventDto.class);
    }
}
