package com.getkhaki.api.bff.config.interceptors.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Accessors(chain = true)
@Getter
@Setter
public class SessionTenant {
    public static final String HEADER_KEY = "KHAKI-TENANT";
    public static final String CLAIMS_KEY = "https://getkhaki.com/tenantIds";
    public static final String CLAIMS_EMAIL_KEY = "https://getkhaki.com/email";
    private UUID tenantId;
    private String tenantName;
}
