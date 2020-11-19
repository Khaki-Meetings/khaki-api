package com.getkhaki.api.bff.persistence;


import com.getkhaki.api.bff.domain.models.OrganizerDm;
import com.getkhaki.api.bff.domain.models.OrganizerStatisticsDm;
import com.getkhaki.api.bff.domain.services.OrganizersStatisticsPersistenceService;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.OrganizerStatisticsDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizersStatisticsRepositoryInterface;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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
    public void getOrganizersStatistics() {


        ZonedDateTime startTest = ZonedDateTime.parse("2020-11-01T00:00:00.000000-07:00[America/Denver]");
        ZonedDateTime endTest = ZonedDateTime.parse("2020-11-12T12:22:40.274456-07:00[America/Denver]");
        int count = 5;

        EmailDao email = new EmailDao()
                .setDomain(
                        new DomainDao()
                                .setName("bob.com")
                )
                .setPerson(
                        new PersonDao()
                        .setFirstName("Bob")
                        .setLastName("Jones")
                )
                .setUser("bob");
        email.setId(UUID.randomUUID());
        email.getDomain().setId(UUID.randomUUID());
        email.getPerson().setId(UUID.randomUUID());

        OrganizerStatisticsDao organizerStatisticsDao = new OrganizerStatisticsDao()
                .setEmail("bob@bob.com")
                .setTotalCost(233)
                .setTotalMeetings(23)
                .setTotalHours(23);
        organizerStatisticsDao.setId(UUID.randomUUID());

        OrganizerStatisticsDm organizerStatisticsDm = new OrganizerStatisticsDm(
                OrganizerDm.builder()
                        .email(email.getUser() + "@" + email.getDomain().getName())
                        .name(email.getPerson().getFirstName() + " " + email.getPerson().getLastName())
                        .build(),
                organizerStatisticsDao.getTotalMeetings(),
                organizerStatisticsDao.getTotalCost(),
                organizerStatisticsDao.getTotalHours()
        );

        List<OrganizerStatisticsDao> organizerStatisticsDaoList = Lists.list(organizerStatisticsDao);
        List<OrganizerStatisticsDm> organizerStatisticsDmList = Lists.list(organizerStatisticsDm);
        when(
                modelMapper.map(
                        organizerStatisticsDaoList,
                        new TypeToken<List<OrganizerStatisticsDm>>() {}.getType()
                )
        ).thenReturn(organizerStatisticsDmList);

//        when(organizersStatisticsRepositoryInterface.findOrganizersStatistics(eq(startTest), eq(endTest), eq(count)))
//                .thenReturn(organizerStatisticsDaoList);
//
//        List<OrganizerStatisticsDm> ret = underTest.getOrganizersStatistics(startTest, endTest, count);
//
//        assertThat(ret).isNotNull();
    }
}
