package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
}
