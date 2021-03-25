package com.getkhaki.api.bff.persistence.models.views;

public interface OrganizerStatisticsView {
    String getOrganizerId();

    Integer getTotalMeetings();

    String getOrganizerEmail();

    String getOrganizerFirstName();

    String getOrganizerLastName();

    String getOrganizerAvatarUrl();

    Long getTotalSeconds();

    Double getTotalCost();
}