package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.persistence.models.EmployeeDao;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DmToDaoEmployeeIntegrationTests extends BaseModelMapperIntegrationTests {

    @Test
    public void dmToDao() {
        val employeeDm = buildDm();

        val employeeDao = underTest.map(employeeDm, EmployeeDao.class);

        assertThat(employeeDao.getDepartment().getName()).isEqualTo(employeeDm.getDepartment());
        assertThat(employeeDao.getPerson().getFirstName()).isEqualTo(employeeDm.getFirstName());
        assertThat(employeeDao.getPerson().getLastName()).isEqualTo(employeeDm.getLastName());
        assertThat(employeeDao.getPerson().getEmails().get(0).getEmailString()).isEqualTo(employeeDm.getEmail());
    }

    private EmployeeDm buildDm() {
        return EmployeeDm.builder()
                .email("bob@jones.com")
                .firstName("Bob")
                .lastName("Jones")
                .department("IT")
                .build();
    }
}
