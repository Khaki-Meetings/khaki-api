package com.getkhaki.api.bff.persistence.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import java.time.ZonedDateTime;

@Data
@Entity
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class CalendarEventDao extends EntityBaseDao {
    String googleCalendarId;

    String summary;

    ZonedDateTime created;
}
