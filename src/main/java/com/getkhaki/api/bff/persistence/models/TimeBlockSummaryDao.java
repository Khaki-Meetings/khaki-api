package com.getkhaki.api.bff.persistence.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class TimeBlockSummaryDao extends EntityBaseDao {

    @Column(nullable = false)
    UUID organizationId;

    UUID departmentId;

    String internalExternal;

    Long totalSeconds;

    Integer meetingCount;

    @Column(nullable = false)
    Instant start;

    Instant end;

    Integer numEmployees;

    Integer numWorkdays;

    Integer totalMeetingInternalAttendees;

    Integer totalMeetingAttendees;

    Long meetingLengthSeconds;

    Integer numEmployeesOverTimeThreshold;

}