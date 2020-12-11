package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.domain.services.GoogleClientFactory;
import com.google.api.services.directory.model.User;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
@CommonsLog
public class GoogleDirectoryRepository {
    private final GoogleClientFactory googleClientFactory;

    @Inject
    public GoogleDirectoryRepository(GoogleClientFactory googleClientFactory) {
        this.googleClientFactory = googleClientFactory;
    }

    public List<User> getUsers(String adminEmail) {
        String[] emailParts = adminEmail.split("@");
        assert emailParts.length == 2 : "Email could not be split";

        try {
            return this.googleClientFactory.getDirectoryClient(adminEmail)
                    .users()
                    .list()
                    .setDomain(emailParts[1])
                    .execute()
                    .getUsers();
        } catch (GeneralSecurityException | IOException e) {
            log.error(e.getMessage());

            return Collections.emptyList();
        }
    }
}
