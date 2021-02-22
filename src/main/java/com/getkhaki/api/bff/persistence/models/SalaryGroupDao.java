package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class SalaryGroupDao extends EntityBaseDao {
    Double salary;

    String role;
}
