package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistence.models.Calendar;
import com.getkhaki.api.bff.persistence.models.User;
import com.getkhaki.api.bff.persistence.repositories.GoogleCalendarRepository;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoogleCalendarRepositoryUnitTests {
    @Mock
    private  GoogleCalendarRepository underTest;
    @Mock
    private  ModelMapper modelMapper;
    @Mock
    private  Calendar client;



    @BeforeEach
    public void setup() {
        underTest = new GoogleCalendarRepository(client);
    }

    @Test
    public void getEvents(){


        String adminEmail="";
        Calendar calendar=new Calendar();
        List<Calendar> calendarList=Lists.list(calendar);
        when(underTest.getEvents(adminEmail)).thenReturn(calendarList);
        underTest.getEvents(adminEmail).forEach(calendarFound -> {
            CalendarEventDm dm=modelMapper.map(calendarFound,CalendarEventDm.class);
            assertThat(dm).isNotNull();
        });


    }

}

