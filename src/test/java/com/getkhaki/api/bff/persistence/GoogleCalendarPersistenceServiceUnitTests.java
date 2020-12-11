package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.persistence.repositories.GoogleCalendarRepository;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class GoogleCalendarPersistenceServiceUnitTests {
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private GoogleCalendarRepository googleCalendarRepository;

    @Mock
    private  GoogleDirectoryRepository googleDirectoryRepository;

    @InjectMocks
    private GoogleCalendarPersistenceService underTest;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void getEvents() {
    }
}
