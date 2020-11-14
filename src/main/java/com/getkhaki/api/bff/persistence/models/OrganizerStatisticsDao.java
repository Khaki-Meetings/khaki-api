package com.getkhaki.api.bff.persistence.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrganizerStatisticsDao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @ManyToOne
    EmailDao email;

    @Column
    int totalMeetings;

    @Column
    long totalCost;

    @Column
    int totalMinutes;
}
