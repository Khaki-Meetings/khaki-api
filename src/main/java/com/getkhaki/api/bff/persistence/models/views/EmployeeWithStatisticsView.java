package com.getkhaki.api.bff.persistence.models.views;

import com.getkhaki.api.bff.persistence.models.EmployeeDao;

public interface EmployeeWithStatisticsView {

    EmployeeDao getEmployee();

    Integer getTotalMeetings();

    Long getTotalSeconds();

}
