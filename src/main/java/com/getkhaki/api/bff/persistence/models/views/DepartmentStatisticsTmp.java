package com.getkhaki.api.bff.persistence.models.views;

import java.util.UUID;

public class DepartmentStatisticsTmp {
    public UUID departmentId;
    public String departmentName;
    public Long totalSeconds;
    public Long inventorySecondsAvailable;


    public UUID getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Long getTotalSeconds() {
        return totalSeconds;
    }

    public Long getInventorySecondsAvailable() {
        return inventorySecondsAvailable;
    }
};