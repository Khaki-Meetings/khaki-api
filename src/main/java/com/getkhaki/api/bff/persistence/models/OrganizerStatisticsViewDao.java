package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Table(name = "v_organizer_statistics")
public class OrganizerStatisticsViewDao extends EntityBaseDao {
    Integer totalMeetingHours;
    Integer totalMeetingHourlyCost;
    Integer totalMeetings;
    Double totalCost;
    UUID organizerEmailId;
    String organizerEmail;
    UUID organizationId;
}