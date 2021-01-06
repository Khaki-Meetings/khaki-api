package com.getkhaki.api.bff.persistence.models.views;

import java.time.Instant;

public interface TimeBlockSummaryView {
    Long getTotalSeconds();
    Long getMeetingCount();
}
