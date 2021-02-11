package com.getkhaki.api.bff.web.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
public class OrganizationDto {
    String name;
    String adminEmail;
}
