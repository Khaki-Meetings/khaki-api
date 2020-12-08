package com.getkhaki.api.bff.persistence.models;


import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

//@Data
//@ToString(callSuper = true)
@Entity
@Accessors(chain = true)
//@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class DomainDao extends EntityBaseDao {
    @Column(unique = true)
    String name;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn,
            inverseJoinColumns = @JoinColumn(unique = true)
    )
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    List<OrganizationDao> organizations;

    @OneToMany(mappedBy = "domain")
    List<EmailDao> emails;

//    @Transient
//    OrganizationDao getOrganization() {
//        return this.organizations.get(0);
//    }
//
//    @Transient
//    DomainDao setOrganization(OrganizationDao organization) {
//        organizations.set(0, organization);
//        return this;
//    }
}
