package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;
import com.getkhaki.api.bff.domain.services.KhakiModelMapper;
import com.getkhaki.api.bff.domain.services.PersonService;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import com.getkhaki.api.bff.persistence.repositories.*;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.directory.model.User;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@CommonsLog
public class GoogleCalendarPersistenceService implements CalendarProviderPersistenceInterface {
    private final GoogleCalendarRepository googleCalendarRepository;
    private final GoogleDirectoryRepository googleDirectoryRepository;
    private final EmailRepositoryInterface emailRepository;
    private final DomainRepositoryInterface domainRepository;
    private final PersonRepositoryInterface personRepository;
    private final KhakiModelMapper modelMapper;

    public GoogleCalendarPersistenceService(
            GoogleCalendarRepository googleCalendarRepository,
            GoogleDirectoryRepository googleDirectoryRepository,
            EmailRepositoryInterface emailRepository,
            DomainRepositoryInterface domainRepository,
            PersonRepositoryInterface personRepository,
            KhakiModelMapper modelMapper
    ) {
        this.googleCalendarRepository = googleCalendarRepository;
        this.googleDirectoryRepository = googleDirectoryRepository;
        this.emailRepository = emailRepository;
        this.domainRepository = domainRepository;
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CalendarEventDm> getEvents(String adminEmail, Instant timeAgo) {
        var calendarEventDms = new ArrayList<CalendarEventDm>();

        List<User> users = this.googleDirectoryRepository.getUsers(adminEmail);

        for (User user : users) {

            /* Update event information */

            List<Event> events = this.googleCalendarRepository.getEvents(adminEmail, user.getPrimaryEmail(), timeAgo);
            for (Event event : events) {
                if (event.getStart().getDateTime() != null) {
                    CalendarEventDm dm = this.modelMapper.mapEventToCalendarEventDm(event);
                    calendarEventDms.add(dm);
                }
            }

            /* Update user information */

            if (user.getPrimaryEmail() != null && user.getPrimaryEmail().split("@").length > 1) {
                String username = user.getPrimaryEmail().split("@")[0];
                String domain = user.getPrimaryEmail().split("@")[1];

                DomainDao domainDao = domainRepository.findDistinctByName(domain).get();
                try {
                    EmailDao emailDao = emailRepository.findDistinctByUserAndDomain_Id(username, domainDao.getId()).get();
                    String emailString = emailDao.getEmailString();
                    try {
                        PersonDao personDao = personRepository.findDistinctByEmailsUserAndEmailsDomainName(username, domain);
                        personDao.setAvatarUrl(user.getThumbnailPhotoUrl());
                        personRepository.save(personDao);
                    } catch (NoSuchElementException e) {
                        log.debug("FOUND EMAIL BUT NOT PERSON - EMAIL: " + username + " , DOMAIN " + domainDao.getId());
                    }
                } catch (NoSuchElementException e) {
                    log.debug("COULD NOT FIND USER - EMAIL: " + username + " , DOMAIN " + domainDao.getId());
                } catch (Exception e) {
                    log.debug("FATAL ERROR - EMAIL: " + username + " , DOMAIN " + domainDao.getId());
                }
            }

        }

        return calendarEventDms;
    }
}
