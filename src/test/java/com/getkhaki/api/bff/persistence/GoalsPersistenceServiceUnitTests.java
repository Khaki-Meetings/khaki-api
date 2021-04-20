package com.getkhaki.api.bff.persistence;

import com.getkhaki.api.bff.domain.services.KhakiModelMapper;
import com.getkhaki.api.bff.persistence.repositories.GoalRepositoryInterface;
import com.getkhaki.api.bff.persistence.repositories.OrganizationRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GoalsPersistenceServiceUnitTests {
    @Mock
    private GoalRepositoryInterface goalRepository;
    @Mock
    private OrganizationRepositoryInterface organizationRepository;
    @Mock
    private KhakiModelMapper modelMapper;

    @InjectMocks
    private GoalPersistenceService underTest;

    @BeforeEach
    public void setup() {
        underTest = new GoalPersistenceService(goalRepository, organizationRepository, modelMapper);
    }

    @Test
    public void getGoals() {
    }
}