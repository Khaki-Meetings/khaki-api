package com.getkhaki.api.bff.persistence.models;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

//@Data
@Entity
@Accessors(chain = true)
//@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class OrganizationDao extends EntityBaseDao {
    String name;

    @ManyToMany(mappedBy = "organizations")
    List<DomainDao> domains;

    @OneToMany(mappedBy = "organization")
    List<DepartmentDao> departments;
}
