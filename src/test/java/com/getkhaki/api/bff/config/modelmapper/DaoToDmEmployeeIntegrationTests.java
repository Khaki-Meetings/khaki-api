package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import com.getkhaki.api.bff.persistence.models.FlagDao;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoToDmEmployeeIntegrationTests extends BaseModelMapperIntegrationTests {

    @Test
    public void success() {
        val employeeDao = new EmployeeDao()
                .setDepartment(
                        new DepartmentDao()
                                .setName("HR")
                                .setOrganization(
                                        new OrganizationDao().setName("Red Star")
                                )
                )
                .setPerson(
                        new PersonDao()
                                .setFirstName("Bob")
                                .setLastName("Johnson")
                                .setEmails(
                                        Stream.of(
                                                new EmailDao()
                                                        .setUser("bob")
                                                        .setDomain(new DomainDao().setName("bob.com"))
                                                        .setFlags(
                                                                Stream.of(
                                                                        new FlagDao().setName(FlagDao.CONTACTABLE)
                                                                ).collect(Collectors.toSet())
                                                        )
                                        ).collect(Collectors.toList())
                                )
                );

        val employeeDm = underTest.map(employeeDao, EmployeeDm.class);

        assertThat(employeeDm.getCompanyName()).isEqualTo(employeeDao.getDepartment().getOrganization().getName());
        assertThat(employeeDm.getDepartment()).isEqualTo(employeeDao.getDepartment().getName());
        assertThat(employeeDm.getDepartment()).isEqualTo(employeeDao.getDepartment().getName());
        assertThat(employeeDm.getFirstName()).isEqualTo(employeeDao.getPerson().getFirstName());
        assertThat(employeeDm.getLastName()).isEqualTo(employeeDao.getPerson().getLastName());
        assertThat(employeeDm.getNotify()).isEqualTo(true);
        assertThat(employeeDm.getEmail()).isEqualTo("bob@bob.com");
    }

}
