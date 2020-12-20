package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.persistence.EmployeePersistenceInterface;
import com.getkhaki.api.bff.domain.services.EmployeeService;
import com.getkhaki.api.bff.web.models.EmployeeDto;
import com.getkhaki.api.bff.web.models.EmployeesResponseDto;
import com.getkhaki.api.bff.web.models.UserProfileResponseDto;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RequestMapping("/employees")
@RestController
@CrossOrigin(origins = "*")
public class EmployeeController {
    private final ModelMapper modelMapper;
    private final EmployeeService employeeService;
    private final EmployeePersistenceInterface employeePersistenceService;

    public EmployeeController(
            ModelMapper modelMapper,
            EmployeeService employeeService,
            EmployeePersistenceInterface employeePersistenceService
    ) {
        this.modelMapper = modelMapper;
        this.employeeService = employeeService;
        this.employeePersistenceService = employeePersistenceService;
    }

    @GetMapping()
    public EmployeesResponseDto getEmployees() {
        val employeesResponseDto = new EmployeesResponseDto();

        employeesResponseDto.setEmployees(
                this.employeeService
                        .getEmployees()
                        .stream()
                        .map(employeeDm -> this.modelMapper.map(employeeDm, EmployeeDto.class))
                        .collect(Collectors.toList())
        );

        return employeesResponseDto;
    }

    @GetMapping("/userProfile")
    public UserProfileResponseDto getUserProfile() {
        return modelMapper.map(
                employeePersistenceService.getAuthedEmployee(),
                UserProfileResponseDto.class
        );
    }
}
