package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class FlagDao extends EntityBaseDao {
    public final static String CONTACTABLE = "Contactable";

    @Column(unique = true)
    String name;
}
