package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.services.DepartmentStatisticsPersistenceService;
import com.getkhaki.api.bff.persistence.repositories.DepartmentStatisticsRepositoryInterface;
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
        underTest = new DepartmentStatisticsPersistenceService(modelMapper, departmentStatisticsRepositoryInterface);
    }

    @Test
    public void test() {

//        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
//        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");
//        UUID id = UUID.randomUUID();
//        DepartmentStatisticsDm departmentStatisticsDm = new DepartmentStatisticsDm(id, "", 1, 1, 1, 1);
//        DepartmentStatisticsDao departmentStatisticsDao = new DepartmentStatisticsDao(departmentStatisticsDm.getId(), departmentStatisticsDm.getDepartment(), departmentStatisticsDm.getMeetingCount(), departmentStatisticsDm.getTotalHours(), departmentStatisticsDm.getTotalCost(), departmentStatisticsDm.getAverageCost());
//
//        Mockito.lenient().when(modelMapper.map(departmentStatisticsDao, DepartmentStatisticsDm.class)).thenReturn(departmentStatisticsDm);
//        Mockito.lenient().when(departmentStatisticsRepositoryInterface.findDepartmentStatisticsInRange(startTest,endTest)).thenReturn(departmentStatisticsDao);
//        List<DepartmentStatisticsDm> ret = underTest.getPerDepartmentStatistics(startTest, endTest);
//        assertThat(ret).isNotNull();
//        assertThat(ret).isEqualTo(departmentStatisticsDm);
    }
}
