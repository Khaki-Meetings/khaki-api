package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.persistence.GoalPersistenceInterface;
import org.junit.jupiter.api.BeforeEach;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class GoalsServiceUnitTests {

    private GoalService underTest;
    private GoalPersistenceInterface goalPersistenceService;
    private SessionTenant sessionTenant;

    @BeforeEach
    public void setup() {
        goalPersistenceService = mock(GoalPersistenceInterface.class);
        sessionTenant = new SessionTenant().setTenantId(UUID.randomUUID());
        underTest = new GoalService(goalPersistenceService, sessionTenant);
    }

}
