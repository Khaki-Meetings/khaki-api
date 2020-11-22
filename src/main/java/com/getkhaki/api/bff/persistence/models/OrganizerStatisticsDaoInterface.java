package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


public interface OrganizerStatisticsDaoInterface {
    String getFirstName();
    String getLastName();
    String getEmail();
    int getTotalMeetings();
//    int getTotalParticipants();
    long getTotalCost();
    int getTotalHours();
}