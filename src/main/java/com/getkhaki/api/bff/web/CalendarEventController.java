package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.web.models.CalendarEventDto;
import com.google.api.services.calendar.model.Events;
import com.getkhaki.api.bff.domain.services.CalendarEventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/calendar-events")
@RestController
public class CalendarEventController {
    private final CalendarEventService calendarEventService;
    private final ModelMapper modelMapper;

    @Autowired
    public CalendarEventController(CalendarEventService calendarEventService, ModelMapper modelMapper) {
        this.calendarEventService = calendarEventService;
        this.modelMapper = modelMapper;
    }

    // @GetMapping
    // public String getHello() {
    //     return "hello";
    // }

    @PostMapping
    public CalendarEventDto createEvent(@RequestBody CalendarEventDto calendarEventDto) {
        CalendarEventDm calendarEventDm = modelMapper.map(calendarEventDto, CalendarEventDm.class);
        CalendarEventDm calendarEventDmReturn = calendarEventService.createEvent(calendarEventDm);
        return modelMapper.map(calendarEventDmReturn, CalendarEventDto.class);
    }

    @GetMapping
    public List<CalendarEventDto> getEvents(){
        List<CalendarEventDto> events = new ArrayList<>();
        //get events
        Events allEvents = calendarEventService.events().list("primary")
                .setTimeMin(now)
                .setSingleEvents(true)
                .execute();
        List<CalendarEventDm> items = allEvents.getItems();
        //events from calendarEventDm to calendarEventDto
        for(CalendarEventDm item : items){
            events.add(modelMapper.map(calendarEventDmReturn, CalendarEventDto.class));
        }
        return events;
    }
}
