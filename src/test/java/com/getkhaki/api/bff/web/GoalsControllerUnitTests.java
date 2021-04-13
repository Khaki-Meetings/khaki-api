package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.GoalDm;
import com.getkhaki.api.bff.domain.services.GoalService;
import com.getkhaki.api.bff.web.models.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoalsControllerUnitTests {
    private GoalsController underTest;

    @Mock
    private GoalService goalService;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        underTest = new GoalsController(
                this.goalService,
                this.modelMapper
        );
    }
    @Test
    public void getGoals() {

        List<GoalDm> goalList = Lists.list(
                new GoalDm().setId(UUID.randomUUID()).setMeasure(GoalMeasureDte.AttendeesPerMeeting).setGreaterThanOrEqualTo(10),
                new GoalDm().setId(UUID.randomUUID()).setMeasure(GoalMeasureDte.AverageMeetingLength).setLessThanOrEqualTo(1000),
                new GoalDm().setId(UUID.randomUUID()).setMeasure(GoalMeasureDte.StaffTimeInMeetings).setLessThanOrEqualTo(10000));

        when(goalService.getGoals()).thenReturn(goalList);

        List<GoalDto> goalDtos = Lists.list(
                new GoalDto().setId(UUID.randomUUID()).setMeasure(GoalMeasureDte.AttendeesPerMeeting).setGreaterThanOrEqualTo(10),
                new GoalDto().setId(UUID.randomUUID()).setMeasure(GoalMeasureDte.AverageMeetingLength).setLessThanOrEqualTo(1000),
                new GoalDto().setId(UUID.randomUUID()).setMeasure(GoalMeasureDte.StaffTimeInMeetings).setLessThanOrEqualTo(10000)
        );

        when(modelMapper.map(goalList.get(0), GoalDto.class)).thenReturn(goalDtos.get(0));
        when(modelMapper.map(goalList.get(1), GoalDto.class)).thenReturn(goalDtos.get(1));
        when(modelMapper.map(goalList.get(2), GoalDto.class)).thenReturn(goalDtos.get(2));

        GoalsResponseDto result = this.underTest.getGoals();

        GoalsResponseDto expectedResult = new GoalsResponseDto().setGoals(goalDtos);

        assertThat(result.getGoals().size()).isEqualTo(expectedResult.getGoals().size());
        assertThat(result.getGoals().get(0).getMeasure()).isEqualTo(expectedResult.getGoals().get(0).getMeasure());
        assertThat(result.getGoals().get(0).getLessThanOrEqualTo()).isEqualTo(expectedResult.getGoals().get(0).getLessThanOrEqualTo());
        assertThat(result.getGoals().get(0).getGreaterThanOrEqualTo()).isEqualTo(expectedResult.getGoals().get(0).getGreaterThanOrEqualTo());

    }
}
