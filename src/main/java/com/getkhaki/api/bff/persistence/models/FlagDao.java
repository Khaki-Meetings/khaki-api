package com.getkhaki.api.bff.persistence.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class FlagDao extends EntityBaseDao {
    public final static String CONTACTABLE = "Contactable";

    @Column(unique = true)
    String name;
}
