package com.getkhaki.api.bff.domain.services;

import com.getkhaki.api.bff.config.interceptors.models.SessionTenant;
import com.getkhaki.api.bff.domain.models.AliasDm;
import com.getkhaki.api.bff.domain.models.GoalDm;
import com.getkhaki.api.bff.domain.persistence.AliasPersistenceInterface;
import com.getkhaki.api.bff.domain.persistence.GoalPersistenceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AliasService {
    private final AliasPersistenceInterface aliasPersistenceService;

    @Autowired
    public AliasService(AliasPersistenceInterface aliasPersistenceService) {
        this.aliasPersistenceService = aliasPersistenceService;
    }

    public List<AliasDm> getAliases(UUID emailId) {
        return this.aliasPersistenceService.getAliases(emailId).stream().collect(Collectors.toList());
    }

    public AliasDm addAlias(UUID emailId, AliasDm aliasDm) {
        return this.aliasPersistenceService.addAlias(aliasDm, emailId);
    }
}