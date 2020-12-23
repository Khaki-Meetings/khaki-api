package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.DepartmentDm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentPersistenceInterface {
    DepartmentDm upsert(DepartmentDm departmentDm);

    Page<DepartmentDm> getDepartments(Pageable pageable);
}