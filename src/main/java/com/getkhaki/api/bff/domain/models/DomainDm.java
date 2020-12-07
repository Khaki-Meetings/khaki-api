package com.getkhaki.api.bff.domain.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DomainDm {
    @Column(unique = true)
    String name;
}
