package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.DepartmentService;
import com.getkhaki.api.bff.web.models.DepartmentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public Page<DepartmentDto> getDepartments(Pageable pageable) {
        return this.departmentService
                .getDepartments(pageable)
                .map(departmentDm -> modelMapper.map(departmentDm, DepartmentDto.class));
    }
}
