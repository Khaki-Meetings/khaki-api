package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistence.models.Calendar;
import com.getkhaki.api.bff.persistence.models.User;
import com.getkhaki.api.bff.persistence.repositories.GoogleCalendarRepository;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import com.google.api.services.calendar.model.Event;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoogleCalendarPersistanceServiceUnitTests {
    @Mock
    private  GoogleCalendarPersistanceService underTest;
    @Mock
    private  ModelMapper modelMapper;
    @Mock
    private  GoogleCalendarRepository googleCalendarRepository;
    @Mock
    private  GoogleDirectoryRepository googleDirectoryRepository;


    @BeforeEach
    public void setup() {
        underTest = new GoogleCalendarPersistanceService(googleCalendarRepository,googleDirectoryRepository, modelMapper);
    }

    @Test
    public void getUsers(){


        String adminEmail="";

        Event event=new Event();
        List<Event> calendarList=Lists.list(event);
        List<CalendarEventDm> calendarDmsList=new ArrayList<>();
        User user=new User("email");
        List<User> users= Lists.list(user);

        try {
            when(googleCalendarRepository.getEvents(adminEmail)).thenReturn(calendarList);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        when(modelMapper.map(event, CalendarEventDm.class)).thenReturn(new CalendarEventDm());


        /*Answer<Integer> answer = new Answer<>() {
            public Integer answer(InvocationOnMock invocation) {
               return 0;
            }
        };*/

        when(googleDirectoryRepository.getUsers(adminEmail)).thenReturn(users);
//        when(googleDirectoryRepository.getUsers(adminEmail)).thenAnswer(answer);

        for (User userFound : users) {
            try {
                googleCalendarRepository.getEvents(userFound.getEmail()).forEach(calendarFound -> {
                    assertThat(calendarFound).isNotNull();
                    CalendarEventDm dm=modelMapper.map(calendarFound,CalendarEventDm.class);
                    assertThat(dm).isNotNull();
                    calendarDmsList.add(dm);
                });
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       assertThat(calendarDmsList).isNotEmpty();



    }

    @Test
    public void getEvents(){

    }
}

