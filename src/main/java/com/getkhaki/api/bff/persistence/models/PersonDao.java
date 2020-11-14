package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class PersonDao extends EntityBaseDao {
    String firstName;
    String lastName;
}
