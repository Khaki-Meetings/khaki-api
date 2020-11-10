package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.DepartmentStatisticsDm;
import com.getkhaki.api.bff.domain.models.DomainTypeDm;
import com.getkhaki.api.bff.domain.models.EmailDm;
import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;
import com.getkhaki.api.bff.domain.services.DepartmentStatisticsPersistenceService;
import com.getkhaki.api.bff.domain.services.OrganizersStatisticsPersistenceService;
import com.getkhaki.api.bff.persistence.models.DepartmentStatisticsDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.OrganizersStatisticsDao;
import com.getkhaki.api.bff.persistence.repositories.DepartmentStatisticsRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.OrganizersStatisticsRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepartmentStatisticsPersistenceServiceUnitTests {

    private DepartmentStatisticsPersistenceService underTest;

    @Mock
    private DepartmentStatisticsRepositoryInterface departmentStatisticsRepositoryInterface;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        underTest = new DepartmentStatisticsPersistenceService(modelMapper,departmentStatisticsRepositoryInterface);
    }

    @Test
    public void test() {

        ZonedDateTime startTest = ZonedDateTime.parse("2019-03-27T10:15:30");
        ZonedDateTime endTest = ZonedDateTime.now();
        UUID id = UUID.randomUUID();
        DepartmentStatisticsDm departmentStatisticsDm = new DepartmentStatisticsDm(id,"",1,1,1,1);
        DepartmentStatisticsDao departmentStatisticsDao = new DepartmentStatisticsDao(departmentStatisticsDm.getId(),departmentStatisticsDm.getDepartment(),departmentStatisticsDm.getMeetingCount(),departmentStatisticsDm.getTotalHours(),departmentStatisticsDm.getTotalCost(),departmentStatisticsDm.getAverageCost());

        when(modelMapper.map(departmentStatisticsDm, DepartmentStatisticsDao.class)).thenReturn(departmentStatisticsDao);
        when(departmentStatisticsRepositoryInterface.findDepartmentStatisticsInRange(startTest,endTest)).thenReturn(departmentStatisticsDao);
        DepartmentStatisticsDm ret = underTest.getPerDepartmentStatistics(startTest,endTest);
        assertThat(ret).isNotNull();
    }
}
