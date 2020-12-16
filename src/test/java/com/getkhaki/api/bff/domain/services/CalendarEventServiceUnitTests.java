package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.models.OrganizationDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.OrganizationPersistenceInterface;
import lombok.val;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CalendarEventServiceUnitTests {
    private CalendarEventService underTest;
    private CalendarEventPersistenceInterface calendarEventPersistence;
    private OrganizationPersistenceInterface organizationPersistenceService;
    private CalendarProviderPersistenceFactory calendarProviderPersistenceFactory;

    @BeforeEach
    public void setup() {
        calendarEventPersistence = mock(CalendarEventPersistenceInterface.class);
        calendarProviderPersistenceFactory = mock(CalendarProviderPersistenceFactory.class);
        organizationPersistenceService = mock(OrganizationPersistenceInterface.class);
        val calendarEventService = new CalendarEventService(
                calendarEventPersistence,
                calendarProviderPersistenceFactory,
                organizationPersistenceService
        );

        underTest = spy(calendarEventService);
    }

    @Test
    public void test() {
        CalendarEventDm calendarEventDmInput = new CalendarEventDm();
        calendarEventDmInput.setSummary("kid gloves");
        calendarEventDmInput.setCreated(Instant.now());

        UUID id = UUID.randomUUID();
        CalendarEventDm calendarEventDmResponse = new CalendarEventDm()
                .setSummary(calendarEventDmInput.getSummary())
                .setGoogleCalendarId(calendarEventDmInput.getGoogleCalendarId())
                .setCreated(calendarEventDmInput.getCreated())
                .setId(id);

        when(calendarEventPersistence.upsert(calendarEventDmInput)).thenReturn(calendarEventDmResponse);

        CalendarEventDm ret = underTest.createEvent(calendarEventDmInput);
        assertThat(ret).isNotNull();
        assertThat(ret.getId()).isEqualTo(id);
        assertThat(ret.getSummary()).isEqualTo(calendarEventDmResponse.getSummary());
        assertThat(ret.getCreated()).isEqualTo(calendarEventDmResponse.getCreated());
    }


    @Test
    public void importAsync() {
        CalendarProviderPersistenceInterface calendarProviderPersistenceInterface = mock(CalendarProviderPersistenceInterface.class);
        when(calendarProviderPersistenceFactory.get()).thenReturn(calendarProviderPersistenceInterface);
        Instant now = Instant.now();

        List<CalendarEventDm> calendarEventDmList = Lists.list(
                new CalendarEventDm()
                        .setCreated(now)
                        .setStart(now)
                        .setEnd(now.plus(1, ChronoUnit.HOURS))
        );


        Answer<Integer> answer = invocation -> {
            List<CalendarEventDm> ret = new ArrayList<>();

            calendarEventDmList.forEach(
                    calendarEventDm -> ret.add(calendarEventPersistence.upsert(calendarEventDm))
            );
            assertThat(ret.size()).isEqualTo(calendarEventDmList.size());
            return ret.size();
        };
        when(calendarProviderPersistenceInterface.getEvents("email")).thenAnswer(answer);
        assertThat(calendarEventDmList).isNotEmpty();
    }

    @Test
    public void importCronTest() {
        doNothing().when(underTest).importAsync(any(String.class));
        Set<OrganizationDm> orgs = Stream.of(
                new OrganizationDm().setName("Working Man").setAdminEmail("joe@workingman.com"),
                new OrganizationDm().setName("Kid Gloves").setAdminEmail("joe@kidgloves.com")
        ).collect(Collectors.toSet());

        Set<String> adminEmails = orgs.stream().map(OrganizationDm::getAdminEmail).collect(Collectors.toSet());

        when(organizationPersistenceService.getImportEnabledOrganizations()).thenReturn(orgs);
        underTest.importCron();
        verify(underTest, times(orgs.size())).importAsync(argThat(adminEmails::contains));
    }
}
