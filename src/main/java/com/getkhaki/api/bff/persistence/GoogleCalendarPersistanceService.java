package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.GoogleCalendarRepository;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import com.google.api.services.calendar.model.Event;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GoogleCalendarPersistanceService implements CalendarProviderPersistenceInterface {

    private final GoogleCalendarRepository googleCalendarRepository;
    private final GoogleDirectoryRepository googleDirectoryRepository;
    private final ModelMapper modelMapper;

    public GoogleCalendarPersistanceService(GoogleCalendarRepository googleCalendarRepository, GoogleDirectoryRepository googleDirectoryRepository, ModelMapper modelMapper) {
        this.googleCalendarRepository = googleCalendarRepository;
        this.googleDirectoryRepository = googleDirectoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CalendarEventDm> getEvents(String adminEmail){
        try {
            List<CalendarEventDm> ret=new ArrayList<>();
            for (Event event : this.googleCalendarRepository.getEvents(adminEmail)) {
                ret.add(this.modelMapper.map(event, CalendarEventDm.class));
            }
            return ret;
        } catch (GeneralSecurityException | IOException e) {
            System.out.println("SOMETHING WENT WRONG: [ \n"+ ExceptionUtils.getStackTrace(e)+" \n]");
            return List.of();
        }
    }

}
