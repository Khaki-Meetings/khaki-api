package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Data
@ToString(callSuper = true)
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class EmailDao extends EntityBaseDao {
    @Column(nullable = false)
    String user;

    @ManyToOne(optional = false)
    DomainDao domain;

    @ManyToMany
    @JoinTable(joinColumns = {@JoinColumn(unique = true)}, inverseJoinColumns = {@JoinColumn})
    List<PersonDao> people = new ArrayList<>();

    @ManyToMany
    Set<FlagDao> flags = new HashSet<>();

    @Transient
    public EmailDao setPerson(PersonDao person) {
        this.getPeople().clear();
        this.getPeople().add(person);
        return this;
    }

    @Transient
    public PersonDao getPerson() {
        return getPeople().get(0);
    }

    @Transient
    public String getEmailString() {
        return getUser() + "@" + getDomain().getName();
    }
}
