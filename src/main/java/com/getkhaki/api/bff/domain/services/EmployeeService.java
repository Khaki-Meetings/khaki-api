package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.domain.persistence.EmployeePersistenceInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    public final EmployeePersistenceInterface employeePersistenceService;

    public EmployeeService(EmployeePersistenceInterface employeePersistenceService) {
        this.employeePersistenceService = employeePersistenceService;
    }

    public List<EmployeeDm> getEmployees() {
        return employeePersistenceService.getEmployees();
    }
}
