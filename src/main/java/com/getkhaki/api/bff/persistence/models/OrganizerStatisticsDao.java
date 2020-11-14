package com.getkhaki.api.bff.persistence.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class OrganizerStatisticsDao extends EntityBaseDao {
    @ManyToOne
    EmailDao email;

    @Column
    int totalMeetings;

    @Column
    long totalCost;

    @Column
    int totalHours;
}
