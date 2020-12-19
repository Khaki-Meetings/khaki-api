package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "organization_id"}))
@Accessors(chain = true)
@Getter
@Setter
public class DepartmentDao extends EntityBaseDao {
    String name;
    @ManyToOne(optional = false)
    OrganizationDao organization;

    @OneToMany(mappedBy = "department")
    List<EmployeeDao> employees;
}
