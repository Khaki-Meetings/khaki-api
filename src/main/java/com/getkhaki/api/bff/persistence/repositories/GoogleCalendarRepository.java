package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.domain.services.GoogleClientFactory;
import com.google.api.services.calendar.model.Event;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
@CommonsLog
public class GoogleCalendarRepository {
    private final GoogleClientFactory googleClientFactory;

    @Inject
    public GoogleCalendarRepository(GoogleClientFactory googleClientFactory) {
        this.googleClientFactory = googleClientFactory;
    }

    public List<Event> getEvents(String adminEmail, String userEmail) {
        try {
            return this.googleClientFactory.getCalendarClient(adminEmail)
                    .events()
                    .list(userEmail)
                    .setSingleEvents(true)
                    .execute()
                    .getItems();
        } catch (GeneralSecurityException | IOException e) {
            log.error(e.getMessage());

            return Collections.emptyList();
        }
    }
}
