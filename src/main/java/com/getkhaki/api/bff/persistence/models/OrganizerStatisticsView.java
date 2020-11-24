package com.getkhaki.api.bff.persistence.models;

public interface OrganizerStatisticsView {
    Integer getTotalMeetingCount();

    String getOrganizerEmail();

    Integer getTotalHours();

    Double getTotalCost();
}