package com.getkhaki.api.bff.persistence.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
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
    public Optional<PersonDao> getPerson() {
        return getPeople().stream().findFirst();
    }

    @Transient
    public String getEmailString() {
        return getUser() + "@" + getDomain().getName();
    }
}
