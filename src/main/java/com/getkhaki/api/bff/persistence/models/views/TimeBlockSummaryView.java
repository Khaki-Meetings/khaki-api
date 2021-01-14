package com.getkhaki.api.bff.persistence.models.views;

import java.time.Instant;
import java.util.UUID;

public interface TimeBlockSummaryView {
    UUID getPersonId();
    UUID getFirstName();
    Long getTotalSeconds();
    Long getMeetingCount();
}
