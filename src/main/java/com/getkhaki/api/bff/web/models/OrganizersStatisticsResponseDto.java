package com.getkhaki.api.bff.web.models;

import com.getkhaki.api.bff.domain.models.EmailDm;
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
public class OrganizersStatisticsResponseDto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    EmailDm email;
    int totalMeetings;
    long totalCost;
    int totalMinutes;
}
