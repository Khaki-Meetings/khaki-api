package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.GoalDm;
import com.getkhaki.api.bff.domain.persistence.GoalPersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GoalService {
    private final GoalPersistenceInterface goalPersistenceService;
    private final SessionTenant sessionTenant;

    @Autowired
    public GoalService(GoalPersistenceInterface goalPersistenceService, SessionTenant sessionTenant) {
        this.goalPersistenceService = goalPersistenceService;
        this.sessionTenant = sessionTenant;
    }

    public List<GoalDm> getGoals() {
        UUID tenantId = sessionTenant.getTenantId();
        return this.goalPersistenceService.getGoals(tenantId).stream().collect(Collectors.toList());
    }

}