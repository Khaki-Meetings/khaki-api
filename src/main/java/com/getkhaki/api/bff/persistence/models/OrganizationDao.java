package com.getkhaki.api.bff.persistence.models;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Entity
@Accessors(chain = true)
@Getter
@Setter
public class OrganizationDao extends EntityBaseDao {
    String name;

    @ManyToMany(cascade = CascadeType.ALL)
    Set<FlagDao> flags;

    @ManyToMany(mappedBy = "organizations")
    List<DomainDao> domains;

    @OneToMany(mappedBy = "organization")
    List<DepartmentDao> departments;
}
