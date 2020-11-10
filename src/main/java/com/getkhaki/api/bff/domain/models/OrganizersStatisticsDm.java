package com.getkhaki.api.bff.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrganizersStatisticsDm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    EmailDm email;
    int totalMeetings;
    long totalCost;
    int totalMinutes;
}
