package com.getkhaki.api.bff.domain.models.Auth0;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMetadata {

    public UserMetadata() {
        this.tenantIdObjects = new ArrayList<Tenant>();
    }

    Properties tenantIds;

    public List<Tenant> getTenantIdObjects() {
        return tenantIdObjects;
    }

    public void setTenantIdObjects(List<Tenant> tenantIdObjects) {
        this.tenantIdObjects = tenantIdObjects;
    }

    List<Tenant> tenantIdObjects;

    public void setTenantIds(Properties tenantIds) { this.tenantIds = tenantIds; }
    public Properties getTenantIds() { return tenantIds; }
}
