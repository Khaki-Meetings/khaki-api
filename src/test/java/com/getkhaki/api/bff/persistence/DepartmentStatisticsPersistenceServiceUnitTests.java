package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.persistence.repositories.DepartmentStatisticsRepositoryInterface;
import liquibase.pro.packaged.S;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class DepartmentStatisticsPersistenceServiceUnitTests {

    private DepartmentStatisticsPersistenceService underTest;

    @Mock
    private DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        underTest = new DepartmentStatisticsPersistenceService(modelMapper, departmentStatisticsRepositoryInterface, new SessionTenant());
    }

    @Test
    public void test() {

    }
}
