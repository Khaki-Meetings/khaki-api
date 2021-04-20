package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.models.GoalDm;
import com.getkhaki.api.bff.domain.persistence.GoalPersistenceInterface;
import com.getkhaki.api.bff.domain.services.KhakiModelMapper;
import com.getkhaki.api.bff.persistence.models.GoalDao;
import com.getkhaki.api.bff.persistence.models.OrganizationDao;
import com.getkhaki.api.bff.persistence.repositories.GoalRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GoalPersistenceService implements GoalPersistenceInterface {

    private final GoalRepositoryInterface goalRepository;
    private final OrganizationRepositoryInterface organizationRepository;
    private final KhakiModelMapper modelMapper;

    @Autowired
    public GoalPersistenceService(GoalRepositoryInterface goalRepository, OrganizationRepositoryInterface organizationRepository, KhakiModelMapper modelMapper) {
        this.goalRepository = goalRepository;
        this.organizationRepository = organizationRepository;
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

    @Override
    public GoalDm addGoal(GoalDm goalDm, UUID tenantId) {

        GoalDao goalDao = this.modelMapper.mapGoalDmToGoal(goalDm);

        this.goalRepository.findDistinctByOrganizationIdAndName(tenantId, goalDao.getName())
            .ifPresent(foundDao -> {
                goalDao.setId(foundDao.getId());
            });

        OrganizationDao organization = this.organizationRepository.findById(tenantId).get();
        goalDao.setOrganization(organization);

        return this.modelMapper.map(
                this.goalRepository.save(goalDao), GoalDm.class);
    }

}
