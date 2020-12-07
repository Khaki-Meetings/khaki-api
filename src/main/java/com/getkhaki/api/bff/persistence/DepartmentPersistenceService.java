package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.DepartmentDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import com.getkhaki.api.bff.persistence.repositories.DepartmentRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentPersistenceService implements DepartmentPersistenceInterface {
    private final DepartmentRepositoryInterface departmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public DepartmentPersistenceService(DepartmentRepositoryInterface departmentRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DepartmentDm createDepartment(DepartmentDm departmentDm) {
        return this.modelMapper.map(
                this.departmentRepository.save(
                        this.modelMapper.map(departmentDm, DepartmentDao.class)
                ), DepartmentDm.class
        );
    }
}
