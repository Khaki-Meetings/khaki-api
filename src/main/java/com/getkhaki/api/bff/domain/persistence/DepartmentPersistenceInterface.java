package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.DepartmentDm;

public interface DepartmentPersistenceInterface {
    DepartmentDm createDepartment(DepartmentDm departmentDm);
}
