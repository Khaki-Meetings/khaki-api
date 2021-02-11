package com.getkhaki.api.bff.persistence.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
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
    List<OrganizationDao> organizations = new ArrayList<>();

    @OneToMany(mappedBy = "domain")
    List<EmailDao> emails = new ArrayList<>();

    @Transient
    public OrganizationDao getOrganization() {
        return this.organizations.get(0);
    }

    @Transient
    public DomainDao setOrganization(OrganizationDao organization) {
        if(!organization.getDomains().contains(this)) {
            organization.getDomains().add(this);
        }

        organizations.clear();
        organizations.add(organization);

        return this;
    }
}
