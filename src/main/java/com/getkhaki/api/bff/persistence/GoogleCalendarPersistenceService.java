package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.GoogleCalendarRepository;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleCalendarPersistenceService implements CalendarProviderPersistenceInterface {
    private final GoogleCalendarRepository googleCalendarRepository;
    private final GoogleDirectoryRepository googleDirectoryRepository;
    private final ModelMapper modelMapper;

    public GoogleCalendarPersistenceService(
            GoogleCalendarRepository googleCalendarRepository,
            GoogleDirectoryRepository googleDirectoryRepository,
            ModelMapper modelMapper
    ) {
        this.googleCalendarRepository = googleCalendarRepository;
        this.googleDirectoryRepository = googleDirectoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CalendarEventDm> getEvents(String adminEmail) {
        return null;
    }
}
