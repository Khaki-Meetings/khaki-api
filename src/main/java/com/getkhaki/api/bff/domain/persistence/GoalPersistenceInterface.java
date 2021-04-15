package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.GoalDm;
import java.util.Set;
import java.util.UUID;

public interface GoalPersistenceInterface {
    Set<GoalDm> getGoals(UUID tenantId);
    GoalDm addGoal(GoalDm goalDm, UUID tenantId);
}
