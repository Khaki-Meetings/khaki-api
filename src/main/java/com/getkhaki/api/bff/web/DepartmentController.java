package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/departments")
@RestController
public class DepartmentController {
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/import")
    public void importAsync(@RequestBody MultipartFile csvFile) throws IOException {
        this.departmentService.importAsync(csvFile.getInputStream());
    }
}
