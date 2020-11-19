package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;

@Entity
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class OrganizerStatisticsDao extends EntityBaseDao {
    String email;

    int totalMeetings;

    long totalCost;

    int totalHours;
}