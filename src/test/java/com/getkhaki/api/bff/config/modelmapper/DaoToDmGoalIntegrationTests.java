package com.getkhaki.api.bff.config.modelmapper;

import com.getkhaki.api.bff.domain.services.KhakiModelMapper;
import com.getkhaki.api.bff.persistence.models.*;
import com.getkhaki.api.bff.web.models.GoalMeasureDte;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DaoToDmGoalIntegrationTests {

    private KhakiModelMapper underTest;

    @BeforeEach
    public void setup() {
        underTest = new KhakiModelMapper();
    }

    @Test
    public void success() {
        val goalDao = new GoalDao()
            .setName("AttendeesPerMeeting")
            .setLessThanOrEqualTo(10);

        val goalDm = underTest.mapGoalToGoalDm(goalDao);

        assertThat(goalDm.getMeasure()).isEqualTo(GoalMeasureDte.AttendeesPerMeeting);
        assertThat(goalDm.getLessThanOrEqualTo()).isEqualTo(goalDao.getLessThanOrEqualTo());
    }

}
