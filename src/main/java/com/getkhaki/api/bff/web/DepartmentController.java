package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.DepartmentService;
import com.getkhaki.api.bff.web.models.DepartmentDto;
import com.getkhaki.api.bff.web.models.DepartmentsResponseDto;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

@RequestMapping("/departments")
@RestController
@CrossOrigin(origins = "*")
public class DepartmentController {
    private final DepartmentService departmentService;
    public final ModelMapper modelMapper;

    @Autowired
    public DepartmentController(DepartmentService departmentService, ModelMapper modelMapper) {
        this.departmentService = departmentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/import")
    public void importAsync(@RequestParam("file") MultipartFile csvFile) throws IOException {
        this.departmentService.csvImport(csvFile.getInputStream());
    }

    @GetMapping
    public DepartmentsResponseDto getDepartments() {
        val departmentResponse = new DepartmentsResponseDto();
        departmentResponse
                .setDepartments(
                        this.departmentService
                                .getDepartments()
                                .stream()
                                .map(
                                        departmentDm -> modelMapper.map(departmentDm, DepartmentDto.class)
                                )
                                .collect(Collectors.toList())
                );
        return departmentResponse;
    }
}
