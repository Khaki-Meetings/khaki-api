package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.DepartmentDm;
import com.getkhaki.api.bff.domain.persistence.DepartmentPersistenceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class DepartmentServiceUnitTests {
    private DepartmentService underTest;
    private CsvFileService csvFileService;
    private DepartmentPersistenceInterface departmentPersistenceService;

    @BeforeEach
    public void setup() {
        csvFileService = mock(CsvFileService.class);
        departmentPersistenceService = mock(DepartmentPersistenceInterface.class);
        DepartmentService departmentService = new DepartmentService(
                csvFileService,
                departmentPersistenceService
        );
        underTest = spy(departmentService);
    }

    @Test
    public void testUpdateDepartment() {
        DepartmentDm departmentDmInput = new DepartmentDm();
        departmentDmInput.setName("original");

        UUID id = UUID.randomUUID();
        DepartmentDm departmentDmResponse = new DepartmentDm()
                .setName(departmentDmInput.getName())
                .setId(id);

        when(departmentPersistenceService.upsert(eq(departmentDmInput)))
            .thenReturn(departmentDmResponse);

        DepartmentDm ret = underTest.upsertDepartment(null, departmentDmInput.getName());
        assertThat(ret).isNotNull();
        assertThat(ret.getName()).isEqualTo("original");

        DepartmentDm ret2 = new DepartmentDm();
        ret2.setId(ret.getId());
        ret2.setName("newName");

        when(departmentPersistenceService.upsert(eq(ret2)))
                .thenReturn(ret2);

        DepartmentDm updateRet = underTest.upsertDepartment(ret.getId(), "newName");
        assertThat(updateRet).isNotNull();
        assertThat(updateRet.getName()).isEqualTo("newName");
    }
}
