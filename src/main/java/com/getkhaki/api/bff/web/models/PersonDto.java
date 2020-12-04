package com.getkhaki.api.bff.web.models;

import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
public class PersonDto {
    UUID id;

    String firstName;

    String lastName;

    String avatarUrl;

    String email;

    Boolean notify;

    String department;
}
