package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.EmployeeDm;

import java.util.List;

public interface EmployeePersistenceInterface {
    List<EmployeeDm> getEmployees();

    EmployeeDm getAuthedEmployee();
}
