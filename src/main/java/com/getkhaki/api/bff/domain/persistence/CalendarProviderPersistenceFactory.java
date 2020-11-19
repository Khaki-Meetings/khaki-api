package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.CalendarEventDm;

public interface CalendarProviderPersistenceFactory {
    CalendarProviderPersistenceInterface get();
}
