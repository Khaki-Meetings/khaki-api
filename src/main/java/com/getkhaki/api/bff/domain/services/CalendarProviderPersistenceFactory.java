package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.persistence.CalendarProviderPersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarProviderPersistenceFactory {
    @Autowired
    CalendarProviderPersistenceInterface calendarProviderPersistenceInterface;

    public CalendarProviderPersistenceInterface get() {
        return this.calendarProviderPersistenceInterface;
    }
}
