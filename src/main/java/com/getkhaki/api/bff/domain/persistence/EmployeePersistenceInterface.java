package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeePersistenceInterface {
    Page<EmployeeDm> getEmployees(Pageable pageable);

    EmployeeDm getAuthedEmployee();

    Page<EmployeeDm> getEmployeesByDepartment(String department, Pageable pageable);

}
