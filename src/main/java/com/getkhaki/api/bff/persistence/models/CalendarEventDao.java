package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class CalendarEventDao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @NonNull
    String summary;
    @NonNull
    LocalDateTime created;
}
