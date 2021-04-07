package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.models.DepartmentDm;
import com.getkhaki.api.bff.domain.models.EmployeeDm;
import com.getkhaki.api.bff.persistence.models.DepartmentDao;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DmToDaoDepartmentIntegrationTests extends BaseModelMapperIntegrationTests {

    @Test
    public void dmToDao() {
        val departmentDm = buildDm();

        val departmentDao = underTest.map(departmentDm, DepartmentDao.class);

        assertThat(departmentDao.getName()).isEqualTo("IT");
        assertThat(departmentDao.getEmployees()).hasSize(2);

    }

    private DepartmentDm buildDm() {
        return DepartmentDm.builder()
            .name("IT")
            .employees(List.of(
                EmployeeDm.builder()
                    .firstName("Steve")
                    .lastName("Jones")
                    .email("steve@jones.com")
                    .build(),

                EmployeeDm.builder()
                    .firstName("Bob")
                    .lastName("Jones")
                    .email("bob@jones.com")
                    .build()
                )
            )
            .build();
    }
}
