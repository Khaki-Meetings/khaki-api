package com.getkhaki.api.bff.persistence.models.views;

import java.util.UUID;

public interface DepartmentStatisticsView {
    UUID getDepartmentId();

    String getDepartmentName();

    Integer getTotalHours();

}
