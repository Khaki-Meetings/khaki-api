package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.domain.persistence.EmployeePersistenceInterface;
import com.getkhaki.api.bff.domain.services.EmployeeService;
import com.getkhaki.api.bff.domain.services.PersonService;
import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import com.getkhaki.api.bff.web.models.EmployeeDto;
import com.getkhaki.api.bff.web.models.EmployeeWithStatisticsDto;
import com.getkhaki.api.bff.web.models.PersonDto;
import com.getkhaki.api.bff.web.models.UserProfileResponseDto;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RequestMapping("/employees")
@RestController
@CrossOrigin(origins = "*")
public class EmployeeController {
    private final ModelMapper modelMapper;
    private final EmployeeService employeeService;
    private final PersonService personService;
    private final EmployeePersistenceInterface employeePersistenceService;

    public EmployeeController(
            ModelMapper modelMapper,
            EmployeeService employeeService,
            PersonService personService,
            EmployeePersistenceInterface employeePersistenceService
    ) {
        this.modelMapper = modelMapper;
        this.employeeService = employeeService;
        this.personService = personService;
        this.employeePersistenceService = employeePersistenceService;
    }

    @GetMapping()
    public Page<EmployeeDto> getEmployees(Pageable pageable,
                  @RequestParam(required = false) String department) {

        if (department == null || department.length() == 0) {
            return this.employeeService
                    .getEmployees(pageable)
                    .map(employeeDm -> this.modelMapper.map(employeeDm, EmployeeDto.class));
        }

        return this.employeeService
                .getEmployeesByDepartment(department, pageable)
                .map(employeeDm -> this.modelMapper.map(employeeDm, EmployeeDto.class));

    }

    @GetMapping("/statistics/{start}/{end}")
    public Page<EmployeeWithStatisticsDto> getEmployeesWithStatistics(
            @PathVariable Instant start,
            @PathVariable Instant end,
            Pageable pageable,
            @RequestParam(required = false) String department) {

        return this.employeeService
                .getEmployeesWithStatistics(start, end, department, pageable)
                .map(employeeDm -> this.modelMapper.map(employeeDm, EmployeeWithStatisticsDto.class));

    }

    @GetMapping("/userProfile")
    public UserProfileResponseDto getUserProfile() {
        val employeeDm = employeePersistenceService.getAuthedEmployee();
        return modelMapper.map(
                employeeDm,
                UserProfileResponseDto.class
        );
    }

    @PutMapping("/userProfile/{id}")
    public UserProfileResponseDto setUserProfile(
            @RequestBody UserProfileResponseDto userProfileResponseDto,
            @PathVariable UUID id) {

        this.employeePersistenceService.updateEmployee(
                id, this.modelMapper.map(userProfileResponseDto, EmployeeDm.class)
        );

        EmployeeDao employeeDao = this.employeePersistenceService.getEmployee(id);
        UserProfileResponseDto result = modelMapper.map(
                employeeDao,
                UserProfileResponseDto.class
        );

        return result;
    }

    @PostMapping("/userProfile")
    public UserProfileResponseDto createUserProfile(
            @RequestBody UserProfileResponseDto userProfileResponseDto) {

        EmployeeDao employeeDao = this.employeePersistenceService.createEmployee(
            userProfileResponseDto.getFirstName(),
            userProfileResponseDto.getLastName(),
            userProfileResponseDto.getEmail(),
            userProfileResponseDto.getDepartment()
        );

        UserProfileResponseDto result = modelMapper.map(
                employeeDao,
                UserProfileResponseDto.class
        );

        return result;

    }
}




