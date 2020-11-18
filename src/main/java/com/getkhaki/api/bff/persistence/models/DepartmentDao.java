package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@Entity
@Accessors(chain = true)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "organization_id"}))
@EqualsAndHashCode(callSuper = true)
public class DepartmentDao extends EntityBaseDao {
    String name;
    @ManyToOne
    OrganizationDao organization;
}
