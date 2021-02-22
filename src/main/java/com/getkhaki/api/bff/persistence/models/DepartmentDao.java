package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "organization_id"}))
public class DepartmentDao extends EntityBaseDao {
    String name;
    @ManyToOne(optional = false)
    OrganizationDao organization;

    @OneToMany(mappedBy = "department")
    List<EmployeeDao> employees = new ArrayList<>();
}
