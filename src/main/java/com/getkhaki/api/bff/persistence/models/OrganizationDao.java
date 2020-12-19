package com.getkhaki.api.bff.persistence.models;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Set;

@Entity
@Accessors(chain = true)
@Getter
@Setter
public class OrganizationDao extends EntityBaseDao {
    String name;

    @OneToOne(optional = false)
    EmailDao adminEmail;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<FlagDao> flags;

    @ManyToMany(mappedBy = "organizations")
    List<DomainDao> domains;

    @OneToMany(mappedBy = "organization")
    List<DepartmentDao> departments;
}
