package com.getkhaki.api.bff.persistence.models.views;

public interface OrganizerStatisticsView {
    Integer getTotalMeetingCount();

    String getOrganizerEmail();

    Integer getTotalHours();

    Double getTotalCost();
}