package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class AliasDao extends EntityBaseDao {
    @Column(nullable = false)
    String alias;

    @ManyToOne(optional = false)
    EmailDao email;
}