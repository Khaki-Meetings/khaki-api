package com.getkhaki.api.bff.persistence.repositories;

import com.getkhaki.api.bff.persistence.models.AliasDao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AliasRepositoryInterface extends JpaRepository<AliasDao, UUID> {

    Optional<AliasDao> findById(UUID uuid);

    Optional<List<AliasDao>> findByEmailId(UUID uuid);

}
