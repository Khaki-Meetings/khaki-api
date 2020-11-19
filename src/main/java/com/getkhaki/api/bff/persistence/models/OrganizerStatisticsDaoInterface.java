package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


public interface OrganizerStatisticsDaoInterface {
    String getFirstName();
    String getLastName();
    String getEmail();
    int getTotalMeetings();
    long getTotalCost();
    int getTotalHours();
}