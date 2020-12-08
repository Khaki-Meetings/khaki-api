package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.domain.models.DepartmentDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import com.getkhaki.api.bff.persistence.repositories.DepartmentRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentPersistenceService implements DepartmentPersistenceInterface {
    private final DepartmentRepositoryInterface departmentRepository;
    private final ModelMapper modelMapper;
    private final SessionTenant sessionTenant;

    @Autowired
    public DepartmentPersistenceService(
            DepartmentRepositoryInterface departmentRepository,
            ModelMapper modelMapper,
            SessionTenant sessionTenant
    ) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
        this.sessionTenant = sessionTenant;
    }

    @Override
    public DepartmentDm createDepartment(DepartmentDm departmentDm) {
        return this.modelMapper.map(
                this.departmentRepository.save(
                        this.modelMapper.map(departmentDm, DepartmentDao.class)
                ), DepartmentDm.class
        );
    }

    @Override
    public List<DepartmentDm> getDepartments() {
        return departmentRepository
                .findDistinctByOrganizationId(sessionTenant.getTenantId())
                .stream()
                .map(
                        departmentDao -> modelMapper.map(departmentDao, DepartmentDm.class)
                )
                .collect(Collectors.toList());
    }

}
