package com.getkhaki.api.bff.persistence.models.views;

public interface OrganizerStatisticsView {
    Integer getTotalMeetings();

    String getOrganizerEmail();

    String getOrganizerFirstName();

    String getOrganizerLastName();

    Long getTotalSeconds();

    Double getTotalCost();
}