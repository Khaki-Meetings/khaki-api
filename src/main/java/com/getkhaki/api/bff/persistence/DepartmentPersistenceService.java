package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.DepartmentDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import com.getkhaki.api.bff.persistence.repositories.DepartmentRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.EmployeeRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import lombok.extern.apachecommons.CommonsLog;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommonsLog
@Service
public class DepartmentPersistenceService implements DepartmentPersistenceInterface {
    private final DepartmentRepositoryInterface departmentRepository;
    private final OrganizationRepositoryInterface organizationRepository;
    private final EmployeeRepositoryInterface employeeRepository;
    private final PersonDaoService personDaoService;
    private final ModelMapper modelMapper;
    private final SessionTenant sessionTenant;

    @Autowired
    public DepartmentPersistenceService(
            DepartmentRepositoryInterface departmentRepository,
            OrganizationRepositoryInterface organizationRepository,
            EmployeeRepositoryInterface employeeRepository, PersonDaoService personDaoService,
            ModelMapper modelMapper,
            SessionTenant sessionTenant
    ) {
        this.departmentRepository = departmentRepository;
        this.organizationRepository = organizationRepository;
        this.employeeRepository = employeeRepository;
        this.personDaoService = personDaoService;
        this.modelMapper = modelMapper;
        this.sessionTenant = sessionTenant;
    }

    @Override
    public DepartmentDm upsert(DepartmentDm departmentDm) {
        val departmentDao = this.modelMapper.map(departmentDm, DepartmentDao.class);
        val organizationDao = organizationRepository
                .findById(sessionTenant.getTenantId()).orElseThrow(() -> new RuntimeException("Organization required"));

        departmentDao.setOrganization(organizationDao);

        val departmentDaoOp = departmentRepository
                .findDistinctByNameAndOrganization(departmentDm.getName(), organizationDao);
        departmentDaoOp.ifPresent(department -> departmentDao.setId(department.getId()));
        departmentRepository.save(departmentDao);

        departmentDao.getEmployees()
                .forEach(
                        employeeDao -> {
                            val savedPersonDao = personDaoService.upsert(employeeDao.getPerson());
                            employeeDao.setPerson(savedPersonDao);
                            employeeDao.setDepartment(departmentDao);

                            employeeRepository.findDistinctByPerson(savedPersonDao)
                                    .orElseGet(() -> employeeRepository.save(employeeDao));
                        }
                );

        return this.modelMapper.map(departmentDao, DepartmentDm.class);
    }

    @Override
    public Page<DepartmentDm> getDepartments(Pageable pageable) {

        List<DepartmentDao> departmentDaoList = departmentRepository
            .findDistinctByOrganizationId(sessionTenant.getTenantId())
            .stream().collect(Collectors.toList());

        // Build this list manually so as to avoid hibernate making a
        // bunch of unnecessary db calls when /departments is requested
        List<DepartmentDm> departmentDmList = new ArrayList<DepartmentDm>();
        for (DepartmentDao departmentDao : departmentDaoList) {
            DepartmentDm departmentDm = new DepartmentDm();
            departmentDm.setId(departmentDao.getId());
            departmentDm.setName(departmentDao.getName());
            departmentDmList.add(departmentDm);
        }

        Page<DepartmentDm> page = new PageImpl<DepartmentDm>(departmentDmList);
        return page;

    }
}
