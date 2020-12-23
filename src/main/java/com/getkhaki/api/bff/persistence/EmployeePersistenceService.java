package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.domain.persistence.EmployeePersistenceInterface;
import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import com.getkhaki.api.bff.persistence.repositories.EmailRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.EmployeeRepositoryInterface;
import com.getkhaki.api.bff.security.AuthenticationFacade;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeePersistenceService implements EmployeePersistenceInterface {
    private final EmployeeRepositoryInterface employeeRepository;
    private final EmailRepositoryInterface emailRepository;
    private final ModelMapper modelMapper;
    private final SessionTenant sessionTenant;
    private final AuthenticationFacade authenticationFacade;
    private final EmailDaoService emailDaoService;

    public EmployeePersistenceService(
            EmployeeRepositoryInterface employeeRepository,
            EmailRepositoryInterface emailRepository,
            ModelMapper modelMapper,
            SessionTenant sessionTenant,
            AuthenticationFacade authenticationFacade,
            EmailDaoService emailDaoService
    ) {
        this.employeeRepository = employeeRepository;
        this.emailRepository = emailRepository;
        this.modelMapper = modelMapper;
        this.sessionTenant = sessionTenant;
        this.authenticationFacade = authenticationFacade;
        this.emailDaoService = emailDaoService;
    }

    @Override
    public Page<EmployeeDm> getEmployees(Pageable pageable) {
        return this.employeeRepository
                .findDistinctByDepartment_OrganizationId(sessionTenant.getTenantId(), pageable)
                .map(employeeDao -> modelMapper.map(employeeDao, EmployeeDm.class));
    }

    @Override
    public EmployeeDm getAuthedEmployee() {
        val authentication = authenticationFacade.getAuthentication();
        val principal = (Jwt) authentication.getPrincipal();

        val emailDao = emailDaoService.upsertByEmailString(principal.getClaim(SessionTenant.CLAIMS_EMAIL_KEY));
        emailDao.getPerson().ifPresentOrElse(
                personDao -> {
                    Optional.of(personDao.getEmployee())
                            .ifPresentOrElse(
                                    employeeDao -> {
                                    },
                                    () -> {
                                        personDao.setEmployee(
                                                new EmployeeDao()
                                                        .setDepartment(new DepartmentDao().setOrganization(new OrganizationDao()))
                                        );
                                    }
                            );
                },
                () -> {
                    val person = new PersonDao()
                            .setEmployee(
                                    new EmployeeDao()
                                            .setDepartment(new DepartmentDao().setOrganization(new OrganizationDao()))
                            )
                            .setEmails(List.of(emailDao));
                    emailDao.setPerson(person);
                }
        );

        return modelMapper.map(emailDao.getPerson().orElseThrow().getEmployee(), EmployeeDm.class);
    }
}
