package com.getkhaki.api.bff.domain.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.getkhaki.api.bff.domain.models.Auth0.Auth0User;
import com.getkhaki.api.bff.domain.models.Auth0.Tenant;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.http.client.utils.URIBuilder;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.util.*;

@CommonsLog
@Service
public class AccessService {

    public List<Auth0User> getUsers(String accessToken, String email) {

        List<Auth0User> users = new ArrayList<Auth0User>();

        try {

            URIBuilder b = new URIBuilder("https://khaki.us.auth0.com/api/v2/users");
            if (email != null && email.length() > 0) {
                b.addParameter("q", "email=" + email);
            }
            URL myurl = b.build().toURL();

            HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Host", "khaki.us.auth0.com");
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

    public String getAccessToken() {
        try {
            URL myurl = new URL("https://khaki.us.auth0.com/oauth/token");
            HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
            String audience = "https://khaki.us.auth0.com/api/v2/";
            String clientId = "HWADegY2pFzzXNo7tP57cjKPI8bYhXAc";
            String clientSecret = "ueUpVMNdJCOGRi0Sxl0F3hvs24G1iCsmX6af_g6Vg6z2oNGbuGOyXO4o4xVqizbV";
            String out = "{\"audience\": \"" + audience + "\","
                    + "\"grant_type\": \"client_credentials\","
                    + "\"client_id\": \"" + clientId + "\","
                    + "\"client_secret\": \"" + clientSecret + "\"}"
                    ;
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
