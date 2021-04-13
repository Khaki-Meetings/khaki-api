package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.GoalDm;
import com.getkhaki.api.bff.domain.persistence.GoalPersistenceInterface;
import com.getkhaki.api.bff.domain.services.KhakiModelMapper;
import com.getkhaki.api.bff.persistence.models.GoalDao;
import com.getkhaki.api.bff.persistence.repositories.GoalRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoalPersistenceService implements GoalPersistenceInterface {

    private final GoalRepositoryInterface goalRepository;
    private final KhakiModelMapper modelMapper;

    @Autowired
    public GoalPersistenceService(GoalRepositoryInterface goalRepository, KhakiModelMapper modelMapper) {
        this.goalRepository = goalRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<GoalDm> getGoals(UUID tenantId) {

        List<GoalDao> organizationGoals = this.goalRepository.findDefaultOrganizationGoals(tenantId);

        List<GoalDao> defaultGoals = this.goalRepository.findDefaultGoals();

        List<GoalDao> results = new ArrayList<GoalDao>();

        for (GoalDao defaultGoal : defaultGoals) {
            GoalDao organizationGoal =  organizationGoals.stream()
                    .filter(g -> defaultGoal.getName().equals(g.getName()))
                    .findAny()
                    .orElse(null);
            if (organizationGoal != null)  {
                results.add(organizationGoal);
            } else {
                results.add(defaultGoal);
            }
        }

        return results.stream()
                .map(goal -> modelMapper.mapGoalToGoalDm(goal))
                .collect(Collectors.toSet());

    }

}
