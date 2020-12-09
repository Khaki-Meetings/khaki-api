package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.models.CalendarEventParticipantDao;
import com.getkhaki.api.bff.persistence.models.DomainDao;
import com.getkhaki.api.bff.persistence.models.EmailDao;
import com.getkhaki.api.bff.persistence.repositories.CalendarEventParticipantRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.CalendarEventRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.DomainRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.EmailRepositoryInterface;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarEventPersistenceService implements CalendarEventPersistenceInterface {
    private final CalendarEventRepositoryInterface calendarEventRepository;
    private final CalendarEventParticipantRepositoryInterface calendarEventParticipantRepository;
    private final EmailRepositoryInterface emailRepository;
    private final DomainRepositoryInterface domainRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CalendarEventPersistenceService(CalendarEventRepositoryInterface calendarEventRepository, CalendarEventParticipantRepositoryInterface calendarEventParticipantRepository, EmailRepositoryInterface emailRepository, DomainRepositoryInterface domainRepository, ModelMapper modelMapper) {
        this.calendarEventRepository = calendarEventRepository;
        this.calendarEventParticipantRepository = calendarEventParticipantRepository;
        this.emailRepository = emailRepository;
        this.domainRepository = domainRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CalendarEventDm createEvent(CalendarEventDm calendarEventDm) {
        val calendarEvent = calendarEventRepository
                .findDistinctByGoogleCalendarId(calendarEventDm.getGoogleCalendarId());
        if (calendarEvent.isPresent()) {
            return modelMapper.map(calendarEvent.get(), CalendarEventDm.class);
        }

        CalendarEventDao calendarEventDao = modelMapper.map(calendarEventDm, CalendarEventDao.class);
        CalendarEventDao calendarEventDaoSaved = calendarEventRepository.save(calendarEventDao);
        calendarEventDm.getParticipants()
                .forEach(
                        calendarEventParticipantDm -> {
                            val email = modelMapper.map(calendarEventParticipantDm.getEmail(), EmailDao.class);
                            DomainDao domain = findOrCreateDomain(email.getDomain());
                            val savedEmail = findOrCreateEmail(email.getUser(), domain);
                            val optionalCalendarEventParticipant = calendarEventParticipantRepository
                                    .findDistinctByCalendarEvent_IdAndEmail_Id(calendarEventDaoSaved.getId(), savedEmail.getId());
                            if (optionalCalendarEventParticipant.isEmpty()) {
                                val calendarEventParticipantDao = new CalendarEventParticipantDao()
                                        .setCalendarEvent(calendarEventDaoSaved)
                                        .setEmail(savedEmail);
                                if (calendarEventParticipantDm.isOrganizer()) {
                                    calendarEventParticipantDao.setOrganizer(true);
                                }
                                calendarEventParticipantRepository.save(calendarEventParticipantDao);

                            }

                        }
                );

        return modelMapper.map(calendarEventDaoSaved, CalendarEventDm.class);
    }

    private DomainDao findOrCreateDomain(final DomainDao domain) {
        return domainRepository.findDistinctByName(domain.getName()).orElseGet(() -> domainRepository.save(domain));
    }

    private EmailDao findOrCreateEmail(final String user, final DomainDao domain) {
        val email = emailRepository
                .findDistinctByUserAndDomain_Id(user, domain.getId());
        if (email.isEmpty()) {
            return emailRepository.save(new EmailDao().setDomain(domain).setUser(user));
        }
//                .orElse(emailRepository.save(new EmailDao().setDomain(domain).setUser(user)));
        return email.get();
    }
}
