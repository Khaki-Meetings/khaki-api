package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.domain.models.TimeBlockRangeDm;

import java.time.Instant;
import java.util.List;

public interface TimeBlockGeneratorInterface {

    List<TimeBlockRangeDm> generate(Instant start, int count);
}
