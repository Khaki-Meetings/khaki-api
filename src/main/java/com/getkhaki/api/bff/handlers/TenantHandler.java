package com.getkhaki.api.bff.handlers;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.security.AuthenticationFacade;
import lombok.val;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

public class TenantHandler implements HandlerInterceptor {
    @Inject
    private SessionTenant sessionTenant;

    @Inject
    private AuthenticationFacade authenticationFacade;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @SuppressWarnings("NullableProblems") HttpServletResponse response,
            @SuppressWarnings("NullableProblems") Object handler
    ) throws RuntimeException {
        Authentication authentication = authenticationFacade.getAuthentication();
        val jwtToken = (Jwt) authentication.getCredentials();

        val tenantName = request.getHeader(SessionTenant.HEADER_KEY);
        if (tenantName == null) {
            throw new RuntimeException(String.format("'%s' header required", SessionTenant.HEADER_KEY));
        }
        sessionTenant.setTenantName(tenantName);

        val claims = jwtToken.getClaims();

        if (claims.containsKey(SessionTenant.CLAIMS_KEY)) {
            val tenantIds = claims.get(SessionTenant.CLAIMS_KEY);
            if (tenantIds instanceof Map) {
                @SuppressWarnings("unchecked") final String tenantId = ((Map<String, String>) tenantIds).get(tenantName);
                sessionTenant.setTenantId(UUID.fromString(tenantId));
            }
        }


        return true;
    }
}
