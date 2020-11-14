package com.getkhaki.api.bff.web.models;

import lombok.Builder;
import lombok.Value;

import java.net.URI;

@Value
@Builder(toBuilder = true)
public class OrganizerDto {
    String name;
    String email;
    URI imageUrl;
}
