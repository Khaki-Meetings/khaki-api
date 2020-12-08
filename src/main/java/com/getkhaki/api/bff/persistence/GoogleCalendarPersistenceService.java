package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;
import com.getkhaki.api.bff.domain.services.KhakiModelMapper;
import com.getkhaki.api.bff.persistence.repositories.GoogleCalendarRepository;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.directory.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleCalendarPersistenceService implements CalendarProviderPersistenceInterface {
    private final GoogleCalendarRepository googleCalendarRepository;
    private final GoogleDirectoryRepository googleDirectoryRepository;
    private final KhakiModelMapper modelMapper;

    public GoogleCalendarPersistenceService(
            GoogleCalendarRepository googleCalendarRepository,
            GoogleDirectoryRepository googleDirectoryRepository,
            KhakiModelMapper modelMapper
    ) {
        this.googleCalendarRepository = googleCalendarRepository;
        this.googleDirectoryRepository = googleDirectoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CalendarEventDm> getEvents(String adminEmail) {
        var calendarEventDms = new ArrayList<CalendarEventDm>();

        List<User> users = this.googleDirectoryRepository.getUsers(adminEmail);

        for(User user: users) {
            List<Event> events = this.googleCalendarRepository.getEvents(adminEmail, user.getPrimaryEmail());

            for(Event event: events) {
                CalendarEventDm dm = this.modelMapper.mapEventToCalendarEventDm(event);

                calendarEventDms.add(dm);
            }
        }

        /*.forEach(user -> this.googleCalendarRepository.getEvents(adminEmail, user.getPrimaryEmail())
                        .forEach(event -> calendarEventDms.add(
                                this.modelMapper.mapEventToCalendarEventDm(event)
                        )));*/

        return calendarEventDms;
    }
}
