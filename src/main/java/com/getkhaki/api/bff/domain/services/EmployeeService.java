package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.domain.models.EmployeeWithStatisticsDm;
import com.getkhaki.api.bff.domain.persistence.EmployeePersistenceInterface;
import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class EmployeeService {
    public final EmployeePersistenceInterface employeePersistenceService;

    public EmployeeService(EmployeePersistenceInterface employeePersistenceService) {
        this.employeePersistenceService = employeePersistenceService;
    }

    public Page<EmployeeDm> getEmployees(Pageable pageable) {
        return employeePersistenceService.getEmployees(pageable);
    }

    public Page<EmployeeDm> getEmployeesByDepartment(String department, Pageable pageable) {
        return employeePersistenceService.getEmployeesByDepartment(department, pageable);
    }

    public Page<EmployeeWithStatisticsDm> getEmployeesWithStatistics(
            Instant sDate, Instant eDate, String department, Pageable pageable) {
        return employeePersistenceService.getEmployeesWithStatistics(
                sDate, eDate, department, pageable);
    }

    public EmployeeDao getEmployee(UUID id) {
        return this.employeePersistenceService.getEmployee(id);
    }

}
