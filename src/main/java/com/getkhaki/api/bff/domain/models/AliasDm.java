package com.getkhaki.api.bff.domain.models;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder(toBuilder = true)
public class AliasDm {
    UUID id;

    String alias;

    String email;
}
