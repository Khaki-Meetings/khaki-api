package com.getkhaki.api.bff.web;

import com.getkhaki.api.bff.domain.models.GoalDm;
import com.getkhaki.api.bff.domain.models.PersonDm;
import com.getkhaki.api.bff.domain.services.GoalService;
import com.getkhaki.api.bff.web.models.GoalDto;
import com.getkhaki.api.bff.web.models.GoalsResponseDto;
import com.getkhaki.api.bff.web.models.PersonDto;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/goals")
public class GoalsController {
    private final GoalService goalService;
    private final ModelMapper modelMapper;

    public GoalsController(GoalService goalService, ModelMapper modelMapper) {
        this.goalService = goalService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public GoalsResponseDto getGoals() {
        GoalsResponseDto goalsResponseDto = new GoalsResponseDto();
        goalsResponseDto.setGoals(goalService.getGoals()
                .stream()
                .map(element -> modelMapper.map(element, GoalDto.class))
                .collect(Collectors.toList()));
        return goalsResponseDto;
    }

    @PostMapping
    public GoalDto addGoal(@RequestBody GoalDto goalDto) {
        return this.modelMapper.map(
                goalService.addGoal(this.modelMapper.map(goalDto, GoalDm.class)),
                GoalDto.class);
    }
}

