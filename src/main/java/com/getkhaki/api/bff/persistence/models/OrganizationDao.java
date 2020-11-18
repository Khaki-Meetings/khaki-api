package com.getkhaki.api.bff.persistence.models;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class OrganizationDao extends EntityBaseDao {
    String name;

    @ManyToMany(mappedBy = "organizations")
    List<DomainDao> domains;

}
