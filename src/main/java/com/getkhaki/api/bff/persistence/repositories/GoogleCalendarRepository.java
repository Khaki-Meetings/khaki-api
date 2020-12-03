package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.Calendar;

import java.util.List;

public class GoogleCalendarRepository {
    private Calendar client;

    public GoogleCalendarRepository(Calendar client) {
        this.client = client;
    }

    public List<Calendar> getEvents(String email){
        return List.of();
    }


}
