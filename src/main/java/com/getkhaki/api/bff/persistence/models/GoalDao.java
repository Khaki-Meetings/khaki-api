package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class GoalDao extends EntityBaseDao {
    String name;
    Integer greaterThanOrEqualTo;
    Integer lessThanOrEqualTo;

    @ManyToOne(optional = true)
    OrganizationDao organization;

    @ManyToOne(optional = true)
    DepartmentDao department;
}
