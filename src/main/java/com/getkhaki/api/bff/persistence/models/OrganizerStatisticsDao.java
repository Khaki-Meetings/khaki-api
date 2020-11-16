package com.getkhaki.api.bff.persistence.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class OrganizerStatisticsDao extends EntityBaseDao {
    EmailDao email;

    int totalMeetings;

    long totalCost;

    int totalHours;
}
