package com.getkhaki.api.bff.domain.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.directory.Directory;
import com.google.api.services.directory.DirectoryScopes;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class GoogleClientFactory {
    @Value("${google.service-account.key}")
    private String googleServiceAccountKey;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Khaki-Api";

    public Directory getDirectoryClient(String adminEmail) throws GeneralSecurityException, IOException {
        return new Directory.Builder(this.getTransport(), JSON_FACTORY, this.getCredentials(adminEmail))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public Calendar getCalendarClient(String adminEmail) throws GeneralSecurityException, IOException {
        return new Calendar.Builder(this.getTransport(), JSON_FACTORY, this.getCredentials(adminEmail))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private NetHttpTransport getTransport() throws GeneralSecurityException, IOException {
        return GoogleNetHttpTransport.newTrustedTransport();
    }

    @SuppressWarnings("deprecation")
    private GoogleCredential getCredentials(String adminEmail) throws IOException {
        return GoogleCredential.fromStream(IOUtils.toInputStream(this.googleServiceAccountKey))
                .createScoped(List.of(DirectoryScopes.ADMIN_DIRECTORY_USER_READONLY, CalendarScopes.CALENDAR_READONLY))
                .createDelegated(adminEmail);
    }
}
