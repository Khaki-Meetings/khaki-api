package com.getkhaki.api.bff.persistence.repositories.test_data;

import com.getkhaki.api.bff.persistence.models.ParticipantTypeDao;
import org.assertj.core.util.Lists;

import java.util.List;

public class ParticipantTypeData implements TestDataInterface<ParticipantTypeDao> {
    @Override
    public List<ParticipantTypeDao> getData() {
        return Lists.list(
                new ParticipantTypeDao().setName("Organizer"),
                new ParticipantTypeDao().setName("Participant")
        );
    }
}
