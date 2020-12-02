package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.services.OrganizersStatisticsPersistenceService;
import com.getkhaki.api.bff.persistence.repositories.GoogleCalendarRepository;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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

    }

    @Test
    public void getEvents(){

    }
}

