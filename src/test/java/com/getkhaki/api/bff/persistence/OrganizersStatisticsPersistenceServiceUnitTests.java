package com.getkhaki.api.bff.persistence;


import com.getkhaki.api.bff.domain.models.DomainTypeDm;
import com.getkhaki.api.bff.domain.models.EmailDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.services.OrganizersStatisticsPersistenceService;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.OrganizerStatisticsDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizersStatisticsRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.List;
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
        OrganizerStatisticsDm organizerStatisticsDm = new OrganizerStatisticsDm(id, "test@test.com", 1, 1, 1);
        OrganizerStatisticsDao organizerStatisticsDao = new OrganizerStatisticsDao(organizerStatisticsDm.getId(), modelMapper.map(emailDm, EmailDao.class), organizerStatisticsDm.getTotalMeetings(), organizerStatisticsDm.getTotalCost(), organizerStatisticsDm.getTotalMinutes());

        Mockito.lenient().when(
                modelMapper.map(
                        organizerStatisticsDao,
                        new TypeToken<List<OrganizerStatisticsDm>>() {}.getType()
                )
        ).thenReturn(organizerStatisticsDm);

//        Mockito.lenient().when(organizersStatisticsRepositoryInterface.findOrganizersStatistics(, emailDm.getEmail(), , )).thenReturn(organizerStatisticsDao);

//        OrganizersStatisticsDm ret = underTest.getOrganizerStatistics(, emailDm.getEmail(), , );

//        assertThat(ret).isNotNull();
    }
}
