package com.getkhaki.api.bff.domain.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getkhaki.api.bff.domain.models.Auth0.Auth0User;
import com.getkhaki.api.bff.domain.models.Auth0.Tenant;
import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.persistence.OrganizationPersistenceService;
import com.getkhaki.api.bff.persistence.repositories.EmailRepositoryInterface;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.http.client.utils.URIBuilder;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.util.*;

@CommonsLog
@Service
public class AccessService {

    @Value("${auth0.accessToken.url}")
    private String auth0AccessTokenUrl;

    @Value("${auth0.accessToken.audience}")
    private String auth0AccessTokenAudience;

    @Value("${auth0.accessToken.clientId}")
    private String auth0AccessTokenClientId;

    @Value("${auth0.accessToken.clientSecret}")
    private String auth0AccessTokenClientSecret;

    @Value("${auth0.users.url}")
    private String auth0UsersUrl;

    @Value("${auth0.host}")
    private String auth0Host;

    private final EmailRepositoryInterface emailRepository;
    private final OrganizationPersistenceService organizationPersistenceService;

    public AccessService(EmailRepositoryInterface emailRepository, OrganizationPersistenceService organizationPersistenceService) {
        this.emailRepository = emailRepository;
        this.organizationPersistenceService = organizationPersistenceService;
    }

    public List<Auth0User> getUsers(String accessToken, String email) {

        List<Auth0User> users = new ArrayList<Auth0User>();

        try {

            URIBuilder b = new URIBuilder(auth0UsersUrl);
            if (email != null && email.length() > 0) {
                b.addParameter("q", "email=" + email);
            }
            URL myurl = b.build().toURL();

            HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Host", auth0Host);
            con.setRequestProperty("Authorization","Bearer " + accessToken);
            con.setRequestMethod("GET");
            con.setDoOutput(true);

            DataInputStream input = new DataInputStream( con.getInputStream() );
            StringBuffer inputLine = new StringBuffer();
            String tmp;
            while ((tmp = input.readLine()) != null) {
                inputLine.append(tmp);
            }
            input.close();

            ObjectMapper mapper = new ObjectMapper();
            List<Auth0User> map = mapper.readValue(inputLine.toString(), new TypeReference<List<Auth0User>>(){});
            for (Auth0User u : map) {
                u.populateTenants();
                users.add(u);
            }

        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
        }
        return users;
    }

    public List<Tenant> getValidTenants(String accessToken, String email) {
        List<Auth0User> userList = getUsers(accessToken, email);
        if (userList != null && userList.size() > 0) {
            Auth0User user = userList.get(0);
            return user.getUser_metadata().getTenantIdObjects();
        }
        return new ArrayList<Tenant>();
    }

    public List<OrganizationDm> getOrganizationsByAdminEmail(String email) {
        return this.organizationPersistenceService.getOrganizationsByAdminEmail(email);
    }

    public String getAccessToken() {
        try {
            URL myurl = new URL(auth0AccessTokenUrl);
            HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();

            JSONObject outJson = new JSONObject();
            outJson.put("audience", auth0AccessTokenAudience);
            outJson.put("grant_type", "client_credentials");
            outJson.put("client_id", auth0AccessTokenClientId);
            outJson.put("client_secret", auth0AccessTokenClientSecret);

            String out = outJson.toString();
            int length = out.length();

            con.setFixedLengthStreamingMode(length);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/x-www-form-urlencoded");
            con.setRequestMethod("POST");
            con.setDoOutput(true);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(out);
            output.close();

            DataInputStream input = new DataInputStream( con.getInputStream() );

            StringBuffer inputLine = new StringBuffer();
            String tmp;
            while ((tmp = input.readLine()) != null) {
                inputLine.append(tmp);
            }
            input.close();

            JSONParser parser = new JSONParser(inputLine.toString());
            LinkedHashMap<String, Object> json = parser.object();
            String itemsObject = json.get("access_token").toString();
            return itemsObject;

        } catch (Exception e) {
            log.info("Exception: " + e.getMessage());
        }

        return null;
    }
}
