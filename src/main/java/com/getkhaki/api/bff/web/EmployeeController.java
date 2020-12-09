package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.EmployeeService;
import com.getkhaki.api.bff.web.models.EmployeeDto;
import com.getkhaki.api.bff.web.models.EmployeesResponseDto;
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

    public EmployeeController(ModelMapper modelMapper, EmployeeService employeeService) {
        this.modelMapper = modelMapper;
        this.employeeService = employeeService;
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
}
