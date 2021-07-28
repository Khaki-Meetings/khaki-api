package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.DepartmentDm;
import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DepartmentPersistenceInterface {
    DepartmentDm upsert(DepartmentDm departmentDm);

    Page<DepartmentDm> getDepartments(Pageable pageable);

    DepartmentDao getDepartmentByOrganizationDepartmentName(UUID organizationId, String departmentName);
}