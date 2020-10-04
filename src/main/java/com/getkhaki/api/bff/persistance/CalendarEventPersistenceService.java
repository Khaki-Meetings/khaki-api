package com.getkhaki.api.bff.persistance;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.persistance.models.CalendarEventDao;
import com.getkhaki.api.bff.persistance.repositories.CalendarEventRepositoryInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventPersistenceService implements CalendarEventPersistenceInterface {
    private final CalendarEventRepositoryInterface calendarEventRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CalendarEventPersistenceService(CalendarEventRepositoryInterface calendarEventRepository, ModelMapper modelMapper) {
        this.calendarEventRepository = calendarEventRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CalendarEventDm createEvent(CalendarEventDm calendarEventDm) {
        CalendarEventDao calendarEventDao = modelMapper.map(calendarEventDm, CalendarEventDao.class);
        CalendarEventDao calendarEventDaoSaved = calendarEventRepository.save(calendarEventDao);
        return modelMapper.map(calendarEventDaoSaved, CalendarEventDm.class);
    }
}
