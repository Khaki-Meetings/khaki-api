package com.getkhaki.api.bff.persistence.models.views;

import com.getkhaki.api.bff.persistence.models.IntervalEnumDao;

public interface TimeBlockSummaryView {
    Long getTotalHours();
    Long getMeetingCount();
}
