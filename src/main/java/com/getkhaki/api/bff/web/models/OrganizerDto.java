package com.getkhaki.api.bff.web.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.net.URI;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class OrganizerDto {
    String name;
    String email;
    URI imageUrl;
}
