package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
@Accessors(chain = true)
@Getter
@Setter
public class EmployeeDao extends EntityBaseDao {
    @ManyToOne
    SalaryGroupDao salaryGroup;

    @ManyToOne(optional = false)
    DepartmentDao department;

    @OneToOne(optional = false)
    PersonDao person;


}
