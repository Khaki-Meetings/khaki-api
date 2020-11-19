package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.OrganizerStatisticsDaoInterface;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.inject.Inject;
import java.util.List;

@DataJpaTest
public class OrganizersStatisticsRepositoryInterfaceIntegrationTests {
    @Inject
    private OrganizersStatisticsRepositoryInterface underTest;

    @Inject
    private CalendarEventRepositoryInterface calendarEventRepository;

    @Inject
    SpringLiquibase liquibase;

    @Test
    public void queryTest() throws LiquibaseException {
        loadData();

        List<OrganizerStatisticsDaoInterface> ret = underTest.findOrganizersStatistics("bob");
        OrganizerStatisticsDaoInterface item = ret.get(0);
        String firstName = item.getFirstName();
        String lastName = item.getLastName();
        String email = item.getEmail();
        int totalMeetings = item.getTotalMeetings();
        int totalParticipants = item.getTotalParticipants();
//        Object[] ret = underTest.findOrganizersStatistics("bob");
    }

    private void loadData() throws LiquibaseException {
        liquibase.setChangeLog("classpath:fixtures/liquibase-test-data.yaml");
        liquibase.setShouldRun(true);
        liquibase.afterPropertiesSet();

//        List<CalendarEventDao> calendarEvent = calendarEventRepository.findAll();
//        CalendarEventParticipantDao calendarEventParticipant = new CalendarEventParticipantTestData().getData().get(0);
    }
}
