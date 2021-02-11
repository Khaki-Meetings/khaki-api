package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class EmployeeDao extends EntityBaseDao {
    @ManyToOne
    SalaryGroupDao salaryGroup;

    @ManyToOne(optional = false)
    DepartmentDao department;

    @OneToOne(optional = false)
    PersonDao person;
}
