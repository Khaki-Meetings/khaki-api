package com.getkhaki.api.bff.domain.persistence;

import com.getkhaki.api.bff.domain.models.AliasDm;

import java.util.Set;
import java.util.UUID;

public interface AliasPersistenceInterface {
    Set<AliasDm> getAliases(UUID emailId);
    AliasDm addAlias(AliasDm aliasDm, UUID emailId);
}
