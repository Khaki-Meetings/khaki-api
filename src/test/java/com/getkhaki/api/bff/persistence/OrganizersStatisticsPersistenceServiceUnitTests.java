package com.getkhaki.api.bff.persistence;


import com.getkhaki.api.bff.domain.models.DomainTypeDm;
import com.getkhaki.api.bff.domain.models.EmailDm;
import com.getkhaki.api.bff.domain.models.OrganizersStatisticsDm;
import com.getkhaki.api.bff.domain.services.OrganizersStatisticsPersistenceService;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.OrganizersStatisticsDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizersStatisticsRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class OrganizersStatisticsPersistenceServiceUnitTests {
    private OrganizersStatisticsPersistenceService underTest;

    @Mock
    private OrganizersStatisticsRepositoryInterface organizersStatisticsRepositoryInterface;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        underTest = new OrganizersStatisticsPersistenceService(organizersStatisticsRepositoryInterface, modelMapper);
    }

    @Test
    public void test() {


        EmailDm emailDm = new EmailDm("test", new DomainTypeDm("mail"));

        UUID id = UUID.randomUUID();
        OrganizersStatisticsDm organizersStatisticsDm = new OrganizersStatisticsDm(id, "test@test.com", 1, 1, 1);
        OrganizersStatisticsDao organizersStatisticsDao = new OrganizersStatisticsDao(organizersStatisticsDm.getId(), modelMapper.map(emailDm, EmailDao.class), organizersStatisticsDm.getTotalMeetings(), organizersStatisticsDm.getTotalCost(), organizersStatisticsDm.getTotalMinutes());

        Mockito.lenient().when(modelMapper.map(organizersStatisticsDao, OrganizersStatisticsDm.class)).thenReturn(organizersStatisticsDm);

        Mockito.lenient().when(organizersStatisticsRepositoryInterface.findOrganizerStatisticsByEmail(emailDm.getEmail())).thenReturn(organizersStatisticsDao);

        OrganizersStatisticsDm ret = underTest.getOrganizerStatistics(emailDm.getEmail());

        assertThat(ret).isNotNull();
    }
}
