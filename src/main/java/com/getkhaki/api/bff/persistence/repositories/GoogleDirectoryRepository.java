package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.services.GoogleClientFactory;
import com.getkhaki.api.bff.persistence.OrganizationPersistenceService;
import com.google.api.services.directory.model.User;
import com.google.api.services.directory.model.Users;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@CommonsLog
public class GoogleDirectoryRepository {
    private final GoogleClientFactory googleClientFactory;
    private final OrganizationPersistenceService organizationPersistenceService;

    @Inject
    public GoogleDirectoryRepository(GoogleClientFactory googleClientFactory,
                                     OrganizationPersistenceService organizationPersistenceService) {
        this.googleClientFactory = googleClientFactory;
        this.organizationPersistenceService = organizationPersistenceService;
    }

    public List<User> getUsers(String adminEmail) {
        String[] emailParts = adminEmail.split("@");
        assert emailParts.length == 2 : "Email could not be split";

        try {

            List<User> usersList = new ArrayList<User>();
            String pageToken = null;

            do {
                Users users = this.googleClientFactory.getDirectoryClient(adminEmail)
                        .users()
                        .list()
                        .setDomain(emailParts[1])
                        .setMaxResults(500)
                        .setPageToken(pageToken)
                        .execute();

                pageToken = users.getNextPageToken();
                usersList.addAll(users.getUsers());

            } while(pageToken != null && pageToken.length() > 0);

            return usersList;

        } catch (GeneralSecurityException | IOException e) {
            log.error(e.getMessage());

            return Collections.emptyList();
        }
    }

    public List<User> getUsers() {
        OrganizationDm organizationDm = organizationPersistenceService.getOrganization();
        return this.getUsers(organizationDm.getAdminEmail());
    }

    public boolean isValidGoogleAdminEmail(String adminEmail) {
        String[] emailParts = adminEmail.split("@");
        assert emailParts.length == 2 : "Email could not be split";

        try {

            List<User> usersList = new ArrayList<User>();
            String pageToken = null;

            do {
                Users users = this.googleClientFactory.getDirectoryClient(adminEmail)
                        .users()
                        .list()
                        .setDomain(emailParts[1])
                        .setMaxResults(500)
                        .setPageToken(pageToken)
                        .execute();

                pageToken = users.getNextPageToken();
                usersList.addAll(users.getUsers());

            } while(pageToken != null && pageToken.length() > 0);

            long matches = usersList.stream().filter(
                    x -> x.getIsAdmin() == true
                            && x.getPrimaryEmail() != null
                            && x.getPrimaryEmail().equals(adminEmail)
            ).count();

            if (matches > 0) {
                return true;
            }

        } catch (com.google.api.client.auth.oauth2.TokenResponseException e) {
            log.error(e.getMessage());
            return false;
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (GeneralSecurityException e) {
            log.error(e.getMessage());
            return false;
        }

        return false;
    }
}
