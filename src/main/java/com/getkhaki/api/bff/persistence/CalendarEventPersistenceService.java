package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.CalendarEventDetailDm;
import com.getkhaki.api.bff.domain.models.CalendarEventDm;
import com.getkhaki.api.bff.domain.models.StatisticsFilterDe;
import com.getkhaki.api.bff.domain.persistence.CalendarEventPersistenceInterface;
import com.getkhaki.api.bff.persistence.models.CalendarEventDao;
import com.getkhaki.api.bff.persistence.models.views.CalendarEventsWithAttendeesView;
import com.getkhaki.api.bff.persistence.repositories.CalendarEventParticipantRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.CalendarEventRepositoryInterface;
import lombok.extern.apachecommons.CommonsLog;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Service
@CommonsLog
public class CalendarEventPersistenceService implements CalendarEventPersistenceInterface {
    private final CalendarEventRepositoryInterface calendarEventRepository;
    private final CalendarEventParticipantRepositoryInterface calendarEventParticipantRepository;
    private final EmailDaoService emailDaoService;
    private final ModelMapper modelMapper;
    private final SessionTenant sessionTenant;

    @Autowired
    public CalendarEventPersistenceService(
            CalendarEventRepositoryInterface calendarEventRepository,
            CalendarEventParticipantRepositoryInterface calendarEventParticipantRepository,
            EmailDaoService emailDaoService, ModelMapper modelMapper,
            SessionTenant sessionTenant
    ) {
        this.calendarEventRepository = calendarEventRepository;
        this.calendarEventParticipantRepository = calendarEventParticipantRepository;
        this.emailDaoService = emailDaoService;
        this.modelMapper = modelMapper;
        this.sessionTenant = sessionTenant;
    }

    @Override
    public CalendarEventDm upsert(CalendarEventDm calendarEventDm) {
        log.debug("Upsert " + calendarEventDm.getGoogleCalendarId());
        val calendarEventDao = modelMapper.map(calendarEventDm, CalendarEventDao.class);
        val calendarEventDaoOp = calendarEventRepository
                .findDistinctByGoogleCalendarId(calendarEventDm.getGoogleCalendarId());

        calendarEventDaoOp.ifPresent(calendarEvent -> calendarEventDao.setId(calendarEvent.getId()));

        calendarEventRepository.save(calendarEventDao);

        calendarEventDao.getParticipants().forEach(
                calendarEventParticipantDao -> {
                    if (calendarEventParticipantDao.getEmail() == null) {
                        log.debug("Email is null" + calendarEventParticipantDao.getId().toString());
                    } else {
                        log.debug("Adding " + calendarEventParticipantDao.getEmail() + " to event "
                            + calendarEventDao.getGoogleCalendarId());
                        val savedEmail = emailDaoService.upsert(calendarEventParticipantDao.getEmail());
                        val eventParticipantOp = calendarEventParticipantRepository
                                .findDistinctByCalendarEventAndEmail(calendarEventDao, savedEmail);
                        if (eventParticipantOp.isEmpty()) {
                            calendarEventParticipantDao.setCalendarEvent(calendarEventDao);
                            try {
                                calendarEventParticipantRepository.save(calendarEventParticipantDao);
                            } catch (Exception e) {
                                log.error("Could not add event participant " + calendarEventParticipantDao.getId().toString()
                                        + " to " + calendarEventDao.getGoogleCalendarId());
                            }
                        }
                    }
                }
        );

        return modelMapper.map(calendarEventDao, CalendarEventDm.class);
    }

    @Override
    public Page<CalendarEventDetailDm> getCalendarEvents(Instant sDate, Instant eDate,
            String organizer, String attendee, StatisticsFilterDe filterDe, Pageable pageable) {

        Page<CalendarEventsWithAttendeesView> calendarEventsWithAttendeesViewList;

        Sort sort = pageable.getSort();

        if (sort.isSorted()) {
            Sort.Order sortOrder = sort.stream().findFirst().orElseThrow();
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    JpaSort.unsafe(
                            sortOrder.getDirection(),
                            String.format("(%s)", sortOrder.getProperty())
                    )
            );
        }

        if (organizer != null && !organizer.trim().isEmpty()) {
            UUID organizerUUID = new UUID(0L, 0L);
            organizerUUID = UUID.fromString(organizer);

            if (filterDe == null) {
                calendarEventsWithAttendeesViewList = calendarEventRepository
                        .getAllCalendarEvents(sessionTenant.getTenantId(), sDate, eDate, organizerUUID, pageable);
            } else {
                switch (filterDe) {
                    case Internal:
                        calendarEventsWithAttendeesViewList = calendarEventRepository
                                .getInternalCalendarEvents(sessionTenant.getTenantId(), sDate, eDate, organizerUUID, pageable);
                        break;
                    case External:
                        calendarEventsWithAttendeesViewList = calendarEventRepository
                                .getExternalCalendarEvents(sessionTenant.getTenantId(), sDate, eDate, organizerUUID, pageable);
                        break;
                    default:
                        calendarEventsWithAttendeesViewList = calendarEventRepository
                                .getAllCalendarEvents(sessionTenant.getTenantId(), sDate, eDate, organizerUUID, pageable);
                }
            }

            return calendarEventsWithAttendeesViewList.map(dao -> modelMapper.map(dao, CalendarEventDetailDm.class));
        }

        if (attendee != null && !attendee.trim().isEmpty()) {
            UUID attendeeUUID = new UUID(0L, 0L);
            attendeeUUID = UUID.fromString(attendee);

            if (filterDe == null) {
                calendarEventsWithAttendeesViewList = calendarEventRepository
                        .getAllCalendarEventsForAttendee(sessionTenant.getTenantId(), sDate, eDate, attendeeUUID, pageable);
            } else {
                switch (filterDe) {
                    case Internal:
                        calendarEventsWithAttendeesViewList = calendarEventRepository
                                .getInternalCalendarEventsForAttendee(sessionTenant.getTenantId(), sDate, eDate, attendeeUUID, pageable);
                        break;
                    case External:
                        calendarEventsWithAttendeesViewList = calendarEventRepository
                                .getExternalCalendarEventsForAttendee(sessionTenant.getTenantId(), sDate, eDate, attendeeUUID, pageable);
                        break;
                    default:
                        calendarEventsWithAttendeesViewList = calendarEventRepository
                                .getAllCalendarEventsForAttendee(sessionTenant.getTenantId(), sDate, eDate, attendeeUUID, pageable);
                }
            }

            return calendarEventsWithAttendeesViewList.map(dao -> modelMapper.map(dao, CalendarEventDetailDm.class));

        }

        return new PageImpl<CalendarEventDetailDm>(new ArrayList<CalendarEventDetailDm>());
    }
}
