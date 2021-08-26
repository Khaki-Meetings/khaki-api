package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.Auth0.Tenant;
import com.getkhaki.api.bff.domain.models.OrganizationDm;
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

        /*
         * For now, we are only adding the admin email from the local database. In the
         * future, this is where we'd ask for the users by role, so if an organization
         * had multiple admins in the Khaki database (or whatever role we wanted to set
         * up), they would all get pulled and updated in Auth0 accordingly.
         */
        List<OrganizationDm> orgs = this.accessService.getOrganizationsByAdminEmail(email);
        for (OrganizationDm org : orgs) {
            String[] emailParts = email.split("@");
            assert emailParts.length == 2 : "Email could not be split";
            String adminDomain = emailParts[1].replace('.','_');
            tenantListJson.put(adminDomain, org.getId().toString());
        }

        JSONObject tenantsJson = new JSONObject();
        tenantsJson.put("tenantIds", tenantListJson);
        return tenantsJson.toString();

    }

}
