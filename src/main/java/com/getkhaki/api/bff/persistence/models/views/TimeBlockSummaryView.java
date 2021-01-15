package com.getkhaki.api.bff.persistence.models.views;

import java.util.UUID;

public interface TimeBlockSummaryView {
    UUID getPersonId();
    String getFirstName();
    Long getTotalSeconds();
    Long getMeetingCount();
}
