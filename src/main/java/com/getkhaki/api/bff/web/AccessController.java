package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.Auth0.Tenant;
import com.getkhaki.api.bff.domain.services.AccessService;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/access")
@RestController
@CrossOrigin(origins = "*")
public class AccessController {

    private final ModelMapper modelMapper;
    private final AccessService accessService;

    @Autowired
    public AccessController(AccessService accessService, ModelMapper modelMapper) {
        this.accessService = accessService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{email}")
    public String getTenantIds(@PathVariable String email) {

        String accessToken = this.accessService.getAccessToken();
        List<Tenant> validTenants = this.accessService.getValidTenants(accessToken, email);

        JSONObject tenantListJson = new JSONObject();
        for (Tenant t: validTenants) {
            tenantListJson.put(t.getDomain(), t.getTenantId());
        }
        JSONObject tenantsJson = new JSONObject();
        tenantsJson.put("tenantIds", tenantListJson);
        return tenantsJson.toString();

    }

}
