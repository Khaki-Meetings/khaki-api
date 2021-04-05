package com.getkhaki.api.bff.persistence.models.views;

public interface OrganizerStatisticsAggregateView {
    String getOrganizerId();

    String getOrganizerEmail();

    String getOrganizerFirstName();

    String getOrganizerLastName();

    String getOrganizerAvatarUrl();

    Integer getInternalMeetingCount();

    Integer getInternalMeetingSeconds();

    Integer getExternalMeetingCount();

    Integer getExternalMeetingSeconds();
}