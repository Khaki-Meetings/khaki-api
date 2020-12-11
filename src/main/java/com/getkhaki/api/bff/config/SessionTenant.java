package com.getkhaki.api.bff.config;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SessionTenant {
    public static final String HEADER_KEY = "KHAKI-TENANT";
    public static final String CLAIMS_KEY = "https://getkhaki.com/tenantIds";
    private UUID tenantId;
    private String tenantName;
}
