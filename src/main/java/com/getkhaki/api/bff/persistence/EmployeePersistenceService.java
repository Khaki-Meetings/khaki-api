package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.domain.persistence.EmployeePersistenceInterface;
import com.getkhaki.api.bff.persistence.repositories.EmployeeRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeePersistenceService implements EmployeePersistenceInterface {
    private final EmployeeRepositoryInterface employeeRepository;
    private final ModelMapper modelMapper;
    private final SessionTenant sessionTenant;

    public EmployeePersistenceService(EmployeeRepositoryInterface employeeRepository, ModelMapper modelMapper, SessionTenant sessionTenant) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.sessionTenant = sessionTenant;
    }

    @Override
    public List<EmployeeDm> getEmployees() {
        return this.employeeRepository
                .findDistinctByDepartment_OrganizationId(sessionTenant.getTenantId())
                .stream()
                .map(
                        employeeDao -> modelMapper.map(employeeDao, EmployeeDm.class)
                )
                .collect(Collectors.toList());
    }
}
