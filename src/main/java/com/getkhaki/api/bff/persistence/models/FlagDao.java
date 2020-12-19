package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Accessors(chain = true)
@Getter
@Setter
public class FlagDao extends EntityBaseDao {
    public final static String CONTACTABLE = "Contactable";

    @Column(unique = true)
    String name;
}
