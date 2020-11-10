package com.getkhaki.api.bff.domain.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class TimeBlockSummaryDm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @Temporal(TemporalType.TIMESTAMP)
    ZonedDateTime start;
    @Temporal(TemporalType.TIMESTAMP)
    ZonedDateTime end;
}
