package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.domain.services.GoogleClientFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import lombok.extern.apachecommons.CommonsLog;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
            val yearAgo = Instant.now().plus(-30, ChronoUnit.DAYS);
            val pastYear = new DateTime(ChronoUnit.MILLIS.between(Instant.EPOCH, yearAgo));
            return this.googleClientFactory.getCalendarClient(adminEmail)
                    .events()
                    .list(userEmail)
                    .setTimeMin(pastYear)
                    .setTimeMax(new DateTime(Instant.now().toEpochMilli()))
                    .setSingleEvents(true)
                    .execute()
                    .getItems();
        } catch (GeneralSecurityException | IOException e) {
            log.error(e.getMessage());

            return Collections.emptyList();
        }
    }
}
