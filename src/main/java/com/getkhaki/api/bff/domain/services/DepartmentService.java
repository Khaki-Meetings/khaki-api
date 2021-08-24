package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentPersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {
    private final CsvFileService csvFileService;
    private final DepartmentPersistenceInterface departmentPersistenceService;

    @Autowired
    public DepartmentService(CsvFileService csvFileService, DepartmentPersistenceInterface departmentPersistenceService) {
        this.csvFileService = csvFileService;
        this.departmentPersistenceService = departmentPersistenceService;
    }

    public void csvImport(InputStream csvInputStream) {
        this.csvFileService.read(csvInputStream)
                .forEach(departmentPersistenceService::upsert);
    }

    public Page<DepartmentDm> getDepartments(Pageable pageable) {
        return departmentPersistenceService.getDepartments(pageable);
    }

    public DepartmentDm upsertDepartment(UUID id, String name) {
        DepartmentDm departmentDm = new DepartmentDm();
        departmentDm.setId(id);
        departmentDm.setName(name);
        return departmentPersistenceService.upsert(departmentDm);
    }
}
