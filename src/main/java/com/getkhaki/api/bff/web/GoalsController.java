package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.GoalDm;
import com.getkhaki.api.bff.web.models.GoalDto;
import com.getkhaki.api.bff.web.models.GoalMeasureDte;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/goals")
public class GoalsController {
    private final ModelMapper modelMapper;

    public GoalsController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<GoalDto> getGoals() {

        List<GoalDm> goalDms = new ArrayList<GoalDm>();
        goalDms.add(
                GoalDm.builder()
                    .id(UUID.randomUUID())
                    .measure(GoalMeasureDte.AttendeesPerMeeting)
                    .lessThanOrEqualTo(0)
                    .greaterThanOrEqualTo(8)
                    .departmentName(null)
                    .build());
        goalDms.add(
                GoalDm.builder()
                        .id(UUID.randomUUID())
                        .measure(GoalMeasureDte.AverageMeetingLength)
                        .lessThanOrEqualTo(25)
                        .greaterThanOrEqualTo(35)
                        .departmentName(null)
                        .build());

        return goalDms
                .stream()
                .map(element -> modelMapper.map(element, GoalDto.class))
                .collect(Collectors.toList());
    }

}

