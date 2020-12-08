package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.repositories.GoogleCalendarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import com.google.api.services.calendar.Calendar;

@ExtendWith(MockitoExtension.class)
public class GoogleCalendarRepositoryUnitTests {
    @Mock
    private  ModelMapper modelMapper;
    @Mock
    private Calendar client;
    @InjectMocks
    private  GoogleCalendarRepository underTest;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void getEvents(){
    }
}

