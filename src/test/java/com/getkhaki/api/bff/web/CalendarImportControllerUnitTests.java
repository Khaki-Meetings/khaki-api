package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.services.CalendarEventService;
import com.getkhaki.api.bff.persistence.repositories.GoogleDirectoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;

public class CalendarImportControllerUnitTests {
    private CalendarEventService calendarEventService;
    private CalendarImportController underTest;
    private GoogleDirectoryRepository googleDirectoryRepository;

    @BeforeEach
    public void setup() {
        calendarEventService = mock(CalendarEventService.class);
        googleDirectoryRepository = mock(GoogleDirectoryRepository.class);
        underTest = new CalendarImportController(calendarEventService, googleDirectoryRepository);
    }

    @Test
    public void importAsync() {
    }
}
