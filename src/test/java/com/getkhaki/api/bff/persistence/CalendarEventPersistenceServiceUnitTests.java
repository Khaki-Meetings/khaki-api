package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.repositories.CalendarEventRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalendarEventPersistenceServiceUnitTests {
    private CalendarEventPersistenceService underTest;

    @Mock
    private CalendarEventRepositoryInterface calendarEventRepository;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        underTest = new CalendarEventPersistenceService(calendarEventRepository, modelMapper);
    }

    @Test
    public void test() {
        CalendarEventDm calendarEventDmInput = new CalendarEventDm()
                .setCreated(Instant.now())
                .setSummary("Before the motor laws");
        CalendarEventDao calendarEventDaoMapped = new CalendarEventDao()
                .setCreated(calendarEventDmInput.getCreated())
                .setSummary(calendarEventDmInput.getSummary());
        when(modelMapper.map(calendarEventDmInput, CalendarEventDao.class)).thenReturn(calendarEventDaoMapped);

        CalendarEventDao calendarEventDaoSaved = new CalendarEventDao()
                .setCreated(calendarEventDmInput.getCreated())
                .setSummary(calendarEventDmInput.getSummary());
        calendarEventDaoSaved.setId(UUID.randomUUID());

        when(calendarEventRepository.save(calendarEventDaoMapped)).thenReturn(calendarEventDaoSaved);

        CalendarEventDm calendarEventDmMapped = new CalendarEventDm()
                .setId(calendarEventDaoSaved.getId())
                .setCreated(calendarEventDaoSaved.getCreated())
                .setSummary(calendarEventDaoSaved.getSummary());

        when(modelMapper.map(calendarEventDaoSaved, CalendarEventDm.class)).thenReturn(calendarEventDmMapped);

        CalendarEventDm ret = underTest.createEvent(calendarEventDmInput);

        assertThat(ret).isNotNull();
        assertThat(ret.getId()).isEqualTo(calendarEventDmMapped.getId());
        assertThat(ret.getSummary()).isEqualTo(calendarEventDmMapped.getSummary());
        assertThat(ret.getCreated()).isEqualTo(calendarEventDmMapped.getCreated());
    }
}
