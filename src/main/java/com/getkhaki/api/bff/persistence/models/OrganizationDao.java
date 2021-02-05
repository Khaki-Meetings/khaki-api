package com.getkhaki.api.bff.persistence.models;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class OrganizationDao extends EntityBaseDao {
    String name;

    @OneToOne(optional = false)
    EmailDao adminEmail;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<FlagDao> flags = new HashSet<>();

    @ManyToMany(mappedBy = "organizations")
    List<DomainDao> domains = new ArrayList<>();

    @OneToMany(mappedBy = "organization")
    List<DepartmentDao> departments = new ArrayList<>();
}
