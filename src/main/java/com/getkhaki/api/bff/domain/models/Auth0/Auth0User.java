package com.getkhaki.api.bff.domain.models.Auth0;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Enumeration;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Auth0User {

    String email;
    UserMetadata user_metadata;

    public UserMetadata getUser_metadata() { return user_metadata; }
    public String getEmail() {  return email; }
    public void setEmail(String email) { this.email = email; }

    public void populateTenants() {
        if (user_metadata != null && user_metadata.getTenantIds() != null) {
            List<Tenant> tenants = this.getUser_metadata().getTenantIdObjects();
            @SuppressWarnings("unchecked")
            Enumeration<String> enums = (Enumeration<String>) user_metadata.getTenantIds().propertyNames();
            while (enums.hasMoreElements()) {
                String key = enums.nextElement();
                String value = user_metadata.getTenantIds().getProperty(key);
                Tenant t = new Tenant();
                t.setDomain(key);
                t.setTenantId(value);
                tenants.add(t);
            }
            this.getUser_metadata().setTenantIdObjects(tenants);
        }
    }
}
