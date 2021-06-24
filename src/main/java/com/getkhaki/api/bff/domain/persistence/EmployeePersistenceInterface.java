package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.domain.models.EmployeeWithStatisticsDm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;

public interface EmployeePersistenceInterface {
    Page<EmployeeDm> getEmployees(Pageable pageable);

    EmployeeDm getAuthedEmployee();

    Page<EmployeeDm> getEmployeesByDepartment(String department, Pageable pageable);

    Page<EmployeeWithStatisticsDm> getEmployeesWithStatistics(
            Instant sDate, Instant eDate, String department, Pageable pageable);

}
