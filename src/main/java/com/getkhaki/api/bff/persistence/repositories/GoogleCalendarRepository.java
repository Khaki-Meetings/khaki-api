package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.domain.services.GoogleClientFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import lombok.SneakyThrows;
import lombok.extern.apachecommons.CommonsLog;
import lombok.val;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@CommonsLog
public class GoogleCalendarRepository {
    private final GoogleClientFactory googleClientFactory;

    public GoogleCalendarRepository(GoogleClientFactory googleClientFactory) {
        this.googleClientFactory = googleClientFactory;
    }

    @SneakyThrows
    public List<Event> getEvents(String adminEmail, String userEmail, Instant timeAgo) {
        val pastYear = new DateTime(ChronoUnit.MILLIS.between(Instant.EPOCH, timeAgo));
        return this.googleClientFactory.getCalendarClient(adminEmail)
                .events()
                .list(userEmail)
                .setTimeMin(pastYear)
                .setTimeMax(new DateTime(Instant.now().toEpochMilli()))
                .setSingleEvents(true)
                .execute()
                .getItems();
    }
}
