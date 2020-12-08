package com.getkhaki.api.bff.persistence;


import com.getkhaki.api.bff.config.SessionTenant;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.models.PersonDao;
import com.getkhaki.api.bff.persistence.repositories.OrganizerStatisticsRepositoryInterface;
import com.getkhaki.api.bff.security.AuthenticationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.ZonedDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class OrganizersStatisticsPersistenceServiceUnitTests {
    private OrganizersStatisticsPersistenceService underTest;

    @Mock
    private OrganizerStatisticsRepositoryInterface OrganizerStatisticsRepositoryInterface;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationFacade mockAuthenticationFacade;

    @BeforeEach
    public void setup() {
        underTest = new OrganizersStatisticsPersistenceService(
                OrganizerStatisticsRepositoryInterface,
                modelMapper,
                new SessionTenant()
        );
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

//        OrganizerStatisticsDao organizerStatisticsDao = new OrganizerStatisticsDao()
//                .setEmail("bob@bob.com")
//                .setTotalCost(233)
//                .setTotalMeetings(23)
//                .setTotalHours(23);
//        organizerStatisticsDao.setId(UUID.randomUUID());
//
//        OrganizerStatisticsDm organizerStatisticsDm = new OrganizerStatisticsDm(
//                OrganizerDm.builder()
//                        .email(email.getUser() + "@" + email.getDomain().getName())
//                        .name(email.getPerson().getFirstName() + " " + email.getPerson().getLastName())
//                        .build(),
//                organizerStatisticsDao.getTotalMeetings(),
//                organizerStatisticsDao.getTotalCost(),
//                organizerStatisticsDao.getTotalHours()
//        );
//
//        List<OrganizerStatisticsDao> organizerStatisticsDaoList = Lists.list(organizerStatisticsDao);
//        List<OrganizerStatisticsDm> organizerStatisticsDmList = Lists.list(organizerStatisticsDm);
//        when(
//                modelMapper.map(
//                        organizerStatisticsDaoList,
//                        new TypeToken<List<OrganizerStatisticsDm>>() {}.getType()
//                )
//        ).thenReturn(organizerStatisticsDmList);

//        when(organizersStatisticsRepositoryInterface.findOrganizersStatistics(eq(startTest), eq(endTest), eq(count)))
//                .thenReturn(organizerStatisticsDaoList);
//
//        List<OrganizerStatisticsDm> ret = underTest.getOrganizersStatistics(startTest, endTest, count);
//
//        assertThat(ret).isNotNull();
    }
}
