package com.getkhaki.api.bff.persistence.models;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class DomainDao extends EntityBaseDao {
    @Column(unique = true)
    String name;
}
