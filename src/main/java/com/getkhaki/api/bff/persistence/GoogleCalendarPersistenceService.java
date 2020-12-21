package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;
import com.getkhaki.api.bff.domain.services.KhakiModelMapper;
import com.getkhaki.api.bff.persistence.repositories.GoogleCalendarRepository;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.directory.model.User;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@CommonsLog
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
    public List<CalendarEventDm> getEvents(String adminEmail, Instant timeAgo) {
        var calendarEventDms = new ArrayList<CalendarEventDm>();

        List<User> users = this.googleDirectoryRepository.getUsers(adminEmail);

        var totalEvents = 0;
        for (User user : users) {
            List<Event> events = this.googleCalendarRepository.getEvents(adminEmail, user.getPrimaryEmail(), timeAgo);
            totalEvents += events.size();

            for (Event event : events) {
                if (event.getStart().getDateTime() != null) {
                    CalendarEventDm dm = this.modelMapper.mapEventToCalendarEventDm(event);
                    calendarEventDms.add(dm);
                }
            }
        }

        return calendarEventDms;
    }
}
