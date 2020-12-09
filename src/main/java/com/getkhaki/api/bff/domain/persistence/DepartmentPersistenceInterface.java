package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.DepartmentDm;

import java.util.List;

public interface DepartmentPersistenceInterface {
    DepartmentDm upsert(DepartmentDm departmentDm);


    List<DepartmentDm> getDepartments();
}