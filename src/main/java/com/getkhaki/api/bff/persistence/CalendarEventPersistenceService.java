package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.repositories.CalendarEventParticipantRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.CalendarEventRepositoryInterface;
import lombok.extern.apachecommons.CommonsLog;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@CommonsLog
public class CalendarEventPersistenceService implements CalendarEventPersistenceInterface {
    private final CalendarEventRepositoryInterface calendarEventRepository;
    private final CalendarEventParticipantRepositoryInterface calendarEventParticipantRepository;
    private final EmailDaoService emailDaoService;
    private final ModelMapper modelMapper;

    @Autowired
    public CalendarEventPersistenceService(
            CalendarEventRepositoryInterface calendarEventRepository,
            CalendarEventParticipantRepositoryInterface calendarEventParticipantRepository,
            EmailDaoService emailDaoService, ModelMapper modelMapper
    ) {
        this.calendarEventRepository = calendarEventRepository;
        this.calendarEventParticipantRepository = calendarEventParticipantRepository;
        this.emailDaoService = emailDaoService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CalendarEventDm upsert(CalendarEventDm calendarEventDm) {
        val calendarEventDao = modelMapper.map(calendarEventDm, CalendarEventDao.class);
        val calendarEventDaoOp = calendarEventRepository
                .findDistinctByGoogleCalendarId(calendarEventDm.getGoogleCalendarId());

        calendarEventDaoOp.ifPresent(calendarEvent -> calendarEventDao.setId(calendarEvent.getId()));

        calendarEventRepository.save(calendarEventDao);

        calendarEventDao.getParticipants().forEach(
                calendarEventParticipantDao -> {
                    if (calendarEventParticipantDao.getEmail() == null) {
                        log.debug("lame");
                    }
                    val savedEmail = emailDaoService.upsert(calendarEventParticipantDao.getEmail());
                    val eventParticipantOp = calendarEventParticipantRepository
                            .findDistinctByCalendarEventAndEmail(calendarEventDao, savedEmail);
                    if (eventParticipantOp.isEmpty()) {

                        calendarEventParticipantDao.setCalendarEvent(calendarEventDao);
                        calendarEventParticipantRepository.save(
                                calendarEventParticipantDao
                        );
                    }
                }
        );

        return modelMapper.map(calendarEventDao, CalendarEventDm.class);
    }
}
