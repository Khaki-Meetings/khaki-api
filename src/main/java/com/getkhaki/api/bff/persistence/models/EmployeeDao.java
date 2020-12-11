package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class EmployeeDao extends EntityBaseDao {
    @ManyToOne
    SalaryGroupDao salaryGroup;

    @ManyToOne
    DepartmentDao department;

    @OneToOne
    PersonDao person;


}
