package com.getkhaki.api.bff.persistance;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.persistance.models.CalendarEventDao;
import com.getkhaki.api.bff.persistance.repositories.CalendarEventRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
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
                .created(LocalDateTime.now())
                .summary("Before the motor laws");
        CalendarEventDao calendarEventDaoMapped = new CalendarEventDao()
                .created(calendarEventDmInput.created())
                .summary(calendarEventDmInput.summary());
        when(modelMapper.map(calendarEventDmInput, CalendarEventDao.class)).thenReturn(calendarEventDaoMapped);

        CalendarEventDao calendarEventDaoSaved = new CalendarEventDao()
                .id(UUID.randomUUID())
                .created(calendarEventDmInput.created())
                .summary(calendarEventDmInput.summary());

        when(calendarEventRepository.save(calendarEventDaoMapped)).thenReturn(calendarEventDaoSaved);

        CalendarEventDm calendarEventDmMapped = new CalendarEventDm()
                .id(calendarEventDaoSaved.id())
                .created(calendarEventDaoSaved.created())
                .summary(calendarEventDaoSaved.summary());

        when(modelMapper.map(calendarEventDaoSaved, CalendarEventDm.class)).thenReturn(calendarEventDmMapped);

        CalendarEventDm ret = underTest.createEvent(calendarEventDmInput);

        assertThat(ret).isNotNull();
        assertThat(ret.id()).isEqualTo(calendarEventDmMapped.id());
        assertThat(ret.summary()).isEqualTo(calendarEventDmMapped.summary());
        assertThat(ret.created()).isEqualTo(calendarEventDmMapped.created());
    }
}
