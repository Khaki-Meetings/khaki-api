package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistence.repositories.GoogleCalendarRepository;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import org.modelmapper.ModelMapper;

import java.util.List;

public class GoogleCalendarPersistanceService {

    private final GoogleCalendarRepository googleCalendarRepository;
    private final GoogleDirectoryRepository googleDirectoryRepository;
    private final ModelMapper modelMapper;

    public GoogleCalendarPersistanceService(GoogleCalendarRepository googleCalendarRepository, GoogleDirectoryRepository googleDirectoryRepository, ModelMapper modelMapper) {
        this.googleCalendarRepository = googleCalendarRepository;
        this.googleDirectoryRepository = googleDirectoryRepository;
        this.modelMapper = modelMapper;
    }

    public List<CalendarEventDm> getEvents(String adminEmail){
        return List.of();
    }

}
